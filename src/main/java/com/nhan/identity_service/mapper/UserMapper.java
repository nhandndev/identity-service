package com.nhan.identity_service.mapper;

import com.nhan.identity_service.dto.request.RegisterRequest;
import com.nhan.identity_service.dto.response.UserResponse;
import com.nhan.identity_service.entity.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
  UserResponse toUserResponse(Users user);

  @Mapping(target = "roles", ignore = true)
  Users toUser(RegisterRequest registerRequest);
}
