package com.nhan.identity_service.service;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.nhan.identity_service.dto.request.AuthenticationRequest;
import com.nhan.identity_service.dto.request.LogoutRequest;
import com.nhan.identity_service.dto.request.RegisterRequest;
import com.nhan.identity_service.dto.response.AuthenticationResponse;
import com.nhan.identity_service.dto.response.UserResponse;
import com.nhan.identity_service.entity.InvalidatedTokens;
import com.nhan.identity_service.entity.Users;
import com.nhan.identity_service.enums.Role;
import com.nhan.identity_service.exception.AppException;
import com.nhan.identity_service.exception.ErrorCode;
import com.nhan.identity_service.mapper.UserMapper;
import com.nhan.identity_service.repository.InvalidatedTokenRepository;
import com.nhan.identity_service.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;
    @Autowired
    private InvalidatedTokenRepository invalidatedTokenRepository;
    @Value("${jwt.signerKey}")
    private String SIGN_KEY;

    public UserResponse register(RegisterRequest registerRequest) {
        Users user = userMapper.toUser(registerRequest);
        if (userRepository.existsByuserName(user.getUserName())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        Set<Role> roles = new HashSet<>();
        roles.add(Role.USER);
        user.setRoles(roles);
        user = userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    public AuthenticationResponse login(AuthenticationRequest authenticationRequest) {
        Users user = userRepository.findByuserName(authenticationRequest.getUserName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        boolean authenticated = passwordEncoder.matches(authenticationRequest.getPassword(), user.getPassword());
        if (!authenticated) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        String token = generateToken(user);
        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    public Void Logout(LogoutRequest logoutRequest) {
        try {
            SignedJWT signedJWT = verifyToken(logoutRequest.getToken());
            InvalidatedTokens invalidatedTokens = InvalidatedTokens.builder()
                    .id(signedJWT.getJWTClaimsSet().getJWTID())
                    .expiryTime(signedJWT.getJWTClaimsSet().getExpirationTime())
                    .build();
            invalidatedTokenRepository.save(invalidatedTokens);
            return null;
        } catch (JOSEException | ParseException exception) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
    }

    public SignedJWT verifyToken(String token) throws ParseException, JOSEException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWSVerifier jwsVerifier = new MACVerifier(SIGN_KEY.getBytes());
        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        Boolean verified = signedJWT.verify(jwsVerifier);
        if (!verified || Date.from(Instant.now()).after(expirationTime)) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        return signedJWT;


    }

    public String generateToken(Users user) {
        JWSHeader jwsHeader = new JWSHeader.Builder(JWSAlgorithm.HS256).type(JOSEObjectType.JWT).build();
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .jwtID(UUID.randomUUID().toString())
                .subject(user.getUserName())
                .issuer("DoanNgocNhan")
                .issueTime(Date.from(Instant.now()))
                .expirationTime(Date.from(Instant.now().plusSeconds(3600)))//3600 = 1 hour
                .claim("scope",buildScope(user))
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        try {
            jwsObject.sign(new MACSigner(SIGN_KEY));
        } catch (JOSEException e) {
            throw new AppException(ErrorCode.TOKEN_CANNOT_CREATE);
        }
        return jwsObject.serialize();
    }

    private String buildScope(Users user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (user.getRoles() == null && user.getRoles().isEmpty())  return "";
        var roles = user.getRoles();
        for(var role : roles){
            String roleName = role.name();
            String formatRoleName = roleName.startsWith("ROLE_")?roleName:"ROLE_"+roleName;
            stringJoiner.add(formatRoleName);
        }
        return stringJoiner.toString();
    }
}