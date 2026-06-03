package com.nhan.identity_service.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterRequest {
    @NotBlank
    @Size(min = 4, max =100)
    String userName;
    @NotBlank
    String lastName;
    @NotBlank
    String firstName;
    @NotBlank
    @Email
    String email;
    @NotBlank
    @Size(min = 4 , max = 100)
    String password;
}
