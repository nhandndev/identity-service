package com.nhan.identity_service.service;

import com.nhan.identity_service.dto.request.UserUpdateRequest;
import com.nhan.identity_service.dto.response.UserResponse;
import com.nhan.identity_service.entity.Users;
import com.nhan.identity_service.exception.AppException;
import com.nhan.identity_service.exception.ErrorCode;
import com.nhan.identity_service.mapper.UserMapper;
import com.nhan.identity_service.repository.UserRepository;
import java.util.List;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  @Autowired private UserRepository userRepository;
  @Autowired private UserMapper userMapper;
  @Autowired private PasswordEncoder passwordEncoder;

  @Transactional
  public UserResponse getMyInfo() {
    String userName = SecurityContextHolder.getContext().getAuthentication().getName();
    Users user =
        userRepository
            .findByuserName(userName)
            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    user.setUserName("Lilnhan");
    //transaction trong update user , khi update nhieu buoc , rollback , 1 thang that bai => quay lai thoi diem ban dau
    //profile

    return userMapper.toUserResponse(user);
  }

  public List<UserResponse> listUser() {
    return userRepository.findAll().stream().map(user -> userMapper.toUserResponse(user)).toList();
  }
  @Transactional
  public UserResponse updateUser(UserUpdateRequest userUpdateRequest){
    String userName = SecurityContextHolder.getContext().getAuthentication().getName();
    Users user = userRepository.findByuserName(userName).orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXISTED));
    if (userUpdateRequest.getUserName() != null && !userUpdateRequest.getUserName().isBlank()) {
      user.setUserName(userUpdateRequest.getUserName());
    }
    if (userUpdateRequest.getLastName() != null && !userUpdateRequest.getLastName().isBlank()) {
      user.setLastName(userUpdateRequest.getLastName());
    }
    if (userUpdateRequest.getFirstName() != null && !userUpdateRequest.getFirstName().isBlank()) {
      user.setFirstName(userUpdateRequest.getFirstName());
    }
    if (userUpdateRequest.getEmail() != null && !userUpdateRequest.getEmail().isBlank()) {
      user.setEmail(userUpdateRequest.getEmail());
    }
    if (userUpdateRequest.getPassword() != null && !userUpdateRequest.getPassword().isBlank()) {
       user.setPassword(passwordEncoder.encode(userUpdateRequest.getPassword()));
    }
    return userMapper.toUserResponse(user);
  }
}
