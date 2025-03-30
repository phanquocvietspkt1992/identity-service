package com.example.identity_service.dto.request;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateRequest {
    protected String id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String username;
    private Set<String> roleNames; // Updated to store role IDs instead of raw role names
}
