package com.example.identity_service.services;

import com.example.identity_service.entities.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    Role createRole(String name, List<String> permissionNames);
    List<Role> getAllRoles();
    Role getRoleById(String id);
    Role updateRole(String id, String name, List<String> permissionNames);
    void deleteRole(String id);
    Role findByName(String roleName);
}
