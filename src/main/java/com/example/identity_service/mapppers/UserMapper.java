package com.example.identity_service.mapppers;

import com.example.identity_service.dto.request.UserCreationRequest;
import com.example.identity_service.dto.request.UserUpdateRequest;
import com.example.identity_service.dto.respones.UserResponse;
import com.example.identity_service.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring") // Ensure Spring can detect this as a bean

public interface UserMapper {
    User toUser(UserCreationRequest userCreationRequest);
    User updateUser(@MappingTarget User user, UserUpdateRequest userUpdateRequest);
    UserResponse toUserResponse(User user);
}
