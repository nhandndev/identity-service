package com.nhan.identity_service.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterRequest {
  @NotBlank(message = "Username is required")
  @Size(min = 4, max = 50, message = "Username must be between 4 and 50 characters")
  @Column(name = "username", nullable = false, unique = true)
  String userName;

  @NotBlank(message = "lastName is required")
  @Column(name = "lastName", nullable = false)
  String lastName;

  @NotBlank(message = "firstName is required")
  @Column(name = "firstName", nullable = false)
  String firstName;

  @NotBlank(message = "Email is required")
  @Email
  String email;

  @NotBlank(message = "Password is required")
  @Size(min = 4, message = "Password must be more than 4 word")
  String password;
}
