package com.nhan.identity_service.service;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.nhan.identity_service.dto.request.AuthenticationRequest;
import com.nhan.identity_service.dto.request.RegisterRequest;
import com.nhan.identity_service.dto.response.AuthenticationResponse;
import com.nhan.identity_service.dto.response.UserResponse;
import com.nhan.identity_service.entity.Users;
import com.nhan.identity_service.enums.Role;
import com.nhan.identity_service.exception.AppException;
import com.nhan.identity_service.exception.ErrorCode;
import com.nhan.identity_service.mapper.UserMapper;
import com.nhan.identity_service.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private String SIGN_KEY = "fa59838d080c4a0c3cad5699b0e9c2f3e3077e2d9e6158b5dbd4ea3eb65cdd2a";//move to .evn later
    public UserResponse register(RegisterRequest registerRequest){
        Users user = userMapper.toUser(registerRequest);
        if(userRepository.existsByuserName(user.getUserName())){
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
    public AuthenticationResponse login(AuthenticationRequest authenticationRequest){
        Users user = userRepository.findByuserName(authenticationRequest.getUserName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        boolean authenticated = passwordEncoder.matches(authenticationRequest.getPassword(),user.getPassword());
        if(!authenticated){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        String token = generateToken(user);
        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }
    public String generateToken(Users user) {
        JWSHeader jwsHeader = new JWSHeader.Builder(JWSAlgorithm.HS256).type(JOSEObjectType.JWT).build();
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .jwtID(UUID.randomUUID().toString())
                .subject(user.getUserName())
                .issuer("DoanNgocNhan")
                .issueTime(Date.from(Instant.now()))
                .expirationTime(Date.from(Instant.now().plusSeconds(3600)))//3600 = 1 hour
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
}
