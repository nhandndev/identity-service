package com.nhan.identity_service.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationRequest {
    @NotNull
    private String userName;
    @NotNull
    private String password;
}
