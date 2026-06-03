package com.nhan.identity_service.dto.request;

import jakarta.validation.constraints.NotNull;

public class AuthenticationRequest {
    @NotNull
    private String userName;
    @NotNull
    private String password;
}
