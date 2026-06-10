package com.nhan.identity_service.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequest {
    @Size(min = 4, max = 50, message = "Username must be between 4 and 50 characters")
    String userName;
    String lastName;
    String firstName;
    @Email
    String email;
    @Size(min = 4, message = "Password must be more than 4 word")
    String password;
}
