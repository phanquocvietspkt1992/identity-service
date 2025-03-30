package com.example.identity_service.dto.request;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class RoleRequest {
    private String name;
    private List<String> permissions; // List of permission names
}
