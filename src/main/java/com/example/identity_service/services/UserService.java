package com.example.identity_service.services;

import com.example.identity_service.dto.request.UserCreationRequest;
import com.example.identity_service.dto.request.UserUpdateRequest;
import com.example.identity_service.entities.User;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UserService {
    public User create(UserCreationRequest request);
    public List<User> findAll();
    public User findById(String id);
    public User update(String userId, UserUpdateRequest request);
    public void delete(String id);
    public  User findByUsername(String username);
}
