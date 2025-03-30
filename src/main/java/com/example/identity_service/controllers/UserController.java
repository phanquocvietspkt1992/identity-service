package com.example.identity_service.controllers;

import com.example.identity_service.dto.request.UserCreationRequest;
import com.example.identity_service.dto.request.UserUpdateRequest;
import com.example.identity_service.dto.respones.ApiResponse;
import com.example.identity_service.dto.respones.UserResponse;
import com.example.identity_service.entities.User;
import com.example.identity_service.mapppers.UserMapper;
import com.example.identity_service.services.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {
    UserMapper userMapper;
    UserService userService;


    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@RequestBody @Valid UserCreationRequest request) {
        User user = userService.create(request);
        return ResponseEntity.ok(ApiResponse.success(userMapper.toUserResponse(user)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<User>>> getAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (authentication != null) ? authentication.getName() : "Anonymous";
        String roles = (authentication != null)
                ? authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList().toString()
                : "No Roles";

        log.info("User '{}' with roles {} called GET /users", username, roles);

        List<User> users = userService.findAll();
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> findById(@PathVariable String userId) {
        User user = userService.findById(userId);
        return ResponseEntity.ok(ApiResponse.success(userMapper.toUserResponse(user)));
    }

//    @PutMapping("/{userId}")
//    public ResponseEntity<ApiResponse<UserResponse>> updateUser(@PathVariable String userId, @RequestBody @Valid UserUpdateRequest request) {
//        User updatedUser = userService.update(userId, request);
//        return ResponseEntity.ok(ApiResponse.success(userMapper.toUserResponse(updatedUser)));
//    }
    @PutMapping()
    public ResponseEntity<ApiResponse<UserResponse>> updateUser( @RequestBody @Valid UserUpdateRequest request) {
        User updatedUser = userService.update(request.getId(), request);
        return ResponseEntity.ok(ApiResponse.success(userMapper.toUserResponse(updatedUser)));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable String userId) {
        userService.delete(userId);
        return ResponseEntity.ok(ApiResponse.success("The user has been deleted successfully."));
    }
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<User>> getCurrentUser() {
        // Step 1: Get Authentication from SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Step 2: Check if the user is authenticated
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Step 3: Extract username and fetch user details
        String username = authentication.getName();
        User user = userService.findByUsername(username);

        return ResponseEntity.ok(ApiResponse.success((user)));
    }
}