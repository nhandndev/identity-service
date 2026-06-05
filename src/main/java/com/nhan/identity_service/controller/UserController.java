package com.nhan.identity_service.controller;

import com.nhan.identity_service.dto.response.ApiResponse;
import com.nhan.identity_service.dto.response.UserResponse;
import com.nhan.identity_service.service.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
  @Autowired private UserService userService;

  @GetMapping("/user/my-info")
  ApiResponse<UserResponse> getMyInfo() {
    return ApiResponse.<UserResponse>builder()
        .code(1000)
        .message("get Info Success")
        .result(userService.getMyInfo())
        .build();
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/users")
  ApiResponse<List<UserResponse>> ListUser() {
    var authentication =
        org.springframework.security.core.context.SecurityContextHolder.getContext()
            .getAuthentication();
    return ApiResponse.<List<UserResponse>>builder()
        .code(1000)
        .message("get All User Success")
        .result(userService.listUser())
        .build();
  }
}
