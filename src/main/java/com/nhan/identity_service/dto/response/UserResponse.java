package com.nhan.identity_service.dto.response;


import com.nhan.identity_service.enums.Role;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    Long id;
    String userName;
    String lastName;
    String firstName;
    String email;
    Role role;
    LocalDateTime createdAt;
}
