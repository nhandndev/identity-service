package com.nhan.identity_service.controller;

import com.nhan.identity_service.dto.response.ApiResponse;
import com.nhan.identity_service.dto.response.UserResponse;
import com.nhan.identity_service.service.AuthService;
import com.nhan.identity_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/user/my-info")
    ApiResponse<UserResponse> getMyInfo(){
        return ApiResponse.<UserResponse>builder()
                .code(1000)
                .message("get Info Success")
                .result(userService.getMyInfo())
                .build();
    }
    @GetMapping("/users")
    ApiResponse<List<UserResponse>> ListUser(){
        return ApiResponse.<List<UserResponse>>builder()
                .code(1000)
                .message("get All User Success")
                .result(userService.listUser())
                .build();

    }
}
