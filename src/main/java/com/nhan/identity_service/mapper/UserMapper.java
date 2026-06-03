package com.nhan.identity_service.mapper;


import com.nhan.identity_service.dto.request.RegisterRequest;
import com.nhan.identity_service.dto.response.UserResponse;
import com.nhan.identity_service.entity.Users;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {
    UserResponse toUserResponse(Users user);
    Users toUser(RegisterRequest registerRequest);
}
