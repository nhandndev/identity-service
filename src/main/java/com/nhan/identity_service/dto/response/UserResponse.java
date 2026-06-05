package com.nhan.identity_service.dto.response;

import com.nhan.identity_service.enums.Role;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
  Long id;
  String userName;
  String lastName;
  String firstName;
  String email;
  Set<Role> roles;
  LocalDateTime createdAt;
}
