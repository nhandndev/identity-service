package com.nhan.identity_service.service;

import com.nhan.identity_service.dto.response.UserResponse;
import com.nhan.identity_service.entity.Users;
import com.nhan.identity_service.exception.AppException;
import com.nhan.identity_service.exception.ErrorCode;
import com.nhan.identity_service.mapper.UserMapper;
import com.nhan.identity_service.repository.UserRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    public UserResponse getMyInfo() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        Users user = userRepository.findByuserName(userName).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userMapper.toUserResponse(user);
    }
    public List<UserResponse> listUser() {
    return userRepository.findAll().stream().map(user ->userMapper.toUserResponse(user)).toList();
    }

}