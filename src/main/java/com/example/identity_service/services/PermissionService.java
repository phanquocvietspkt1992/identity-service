package com.example.identity_service.services;
import com.example.identity_service.entities.Permission;
import java.util.List;

public interface PermissionService {
    Permission createPermission(String name);
    List<Permission> getAllPermissions();
    Permission getPermissionById(String id);
    Permission updatePermission(String id, String name);
    void deletePermission(String id);
}
