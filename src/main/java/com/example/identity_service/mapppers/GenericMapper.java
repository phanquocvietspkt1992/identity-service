package com.example.identity_service.mapppers;

import com.example.identity_service.dto.request.UserUpdateRequest;
import com.example.identity_service.dto.respones.UserResponse;
import com.example.identity_service.entities.User;
import org.mapstruct.MappingTarget;

public interface GenericMapper<T, U> {
    void toEntity(T entity, U request);
    T updateEntity(@MappingTarget T t, U u);
    U toResponse(T t);

}
