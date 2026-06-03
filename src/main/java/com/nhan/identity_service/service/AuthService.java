package com.nhan.identity_service.service;

import com.nhan.identity_service.dto.request.RegisterRequest;
import com.nhan.identity_service.dto.response.UserResponse;
import com.nhan.identity_service.entity.Users;
import com.nhan.identity_service.enums.Role;
import com.nhan.identity_service.exception.AppException;
import com.nhan.identity_service.exception.ErrorCode;
import com.nhan.identity_service.mapper.UserMapper;
import com.nhan.identity_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
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
}
