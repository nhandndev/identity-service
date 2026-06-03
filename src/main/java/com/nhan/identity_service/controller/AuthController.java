package com.nhan.identity_service.controller;

import com.nhan.identity_service.dto.request.AuthenticationRequest;
import com.nhan.identity_service.dto.request.RegisterRequest;
import com.nhan.identity_service.dto.response.ApiResponse;
import com.nhan.identity_service.dto.response.AuthenticationResponse;
import com.nhan.identity_service.dto.response.UserResponse;
import com.nhan.identity_service.service.AuthService;
import com.nhan.identity_service.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ApiResponse<UserResponse> register(@RequestBody @Valid RegisterRequest registerRequest){
        return ApiResponse.<UserResponse>builder()
                .code(1000)
                .message("register completed")
                .result(authService.register(registerRequest))
                .build();
    }
    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> login(@RequestBody AuthenticationRequest authenticationRequest){
        return ApiResponse.<AuthenticationResponse>builder()
                .code(1000)
                .message("login completed")
                .result(authService.login(authenticationRequest))
                .build();

    }

}
