package com.example.identity_service.dto.respones;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse
{
    private String id;

    private String firstName;

    private String lastName;
    private String email;
    private String username;
    private String password;
    private Set<String> roleIds;
}
