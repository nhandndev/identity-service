package com.nhan.identity_service.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LogoutRequest {
    @NotNull
    private String token;
}
