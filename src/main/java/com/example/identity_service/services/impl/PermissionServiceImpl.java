package com.example.identity_service.services.impl;

import com.example.identity_service.entities.Permission;
import com.example.identity_service.repositories.PermissionRepository;
import com.example.identity_service.services.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {
    private final PermissionRepository permissionRepository;

    @Override
    public Permission createPermission(String name) {
        Permission permission = new Permission();
        permission.setName(name);
        return permissionRepository.save(permission);
    }

    @Override
    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }

    @Override
    public Permission getPermissionById(String id) {
        return permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permission not found"));
    }

    @Override
    public Permission updatePermission(String id, String name) {
        Permission permission = getPermissionById(id);
        permission.setName(name);
        return permissionRepository.save(permission);
    }

    @Override
    public void deletePermission(String id) {
        permissionRepository.deleteById(id);
    }
}

