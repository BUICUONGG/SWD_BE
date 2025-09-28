package swd.fpt.exegroupingmanagement.service.impl;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import swd.fpt.exegroupingmanagement.constant.PredefinedRole;
import swd.fpt.exegroupingmanagement.dto.request.RoleRequest;
import swd.fpt.exegroupingmanagement.dto.response.RoleResponse;
import swd.fpt.exegroupingmanagement.entity.PermissionEntity;
import swd.fpt.exegroupingmanagement.entity.RoleEntity;
import swd.fpt.exegroupingmanagement.exception.ErrorCode;
import swd.fpt.exegroupingmanagement.exception.exceptions.ResourceConflictException;
import swd.fpt.exegroupingmanagement.exception.exceptions.ResourceNotFoundException;
import swd.fpt.exegroupingmanagement.mapper.RoleMapper;
import swd.fpt.exegroupingmanagement.repository.PermissionRepository;
import swd.fpt.exegroupingmanagement.repository.RoleRepository;
import swd.fpt.exegroupingmanagement.service.PermissionService;
import swd.fpt.exegroupingmanagement.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import static swd.fpt.exegroupingmanagement.exception.ErrorCode.ROLE_NOT_FOUND;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleServiceImpl implements RoleService {
    RoleRepository roleRepository;
    RoleMapper roleMapper;
    PermissionRepository permissionRepository;
    PermissionService permissionService;

    @Override
    public List<RoleResponse> getAllRoles() {
        List<RoleEntity> roles = roleRepository.findAll();
        if (roles.isEmpty()) {
            throw new ResourceNotFoundException(ROLE_NOT_FOUND.getMessage());
        }
        return roles.stream().map(roleMapper::toRoleResponse).toList();
    }

    @Override
    public RoleResponse add(RoleRequest roleRequest) {
        String roleName = roleRequest.getRoleName().toUpperCase(Locale.ROOT);
        RoleEntity role = roleRepository.findByRoleNameAndIsDeletedFalse(roleName)
                .orElseGet(() -> {
                    RoleEntity newRole = roleMapper.toRole(roleRequest);
                    newRole.setRoleName(roleName);
                    newRole.setDeleted(false);
                    newRole.setPermissions(new HashSet<>()); // Khởi tạo Set rỗng cho vai trò mới
                    return newRole;
                });
        var permissions = permissionRepository.findAllById(roleRequest.getPermissions());
        if (permissions.size() != roleRequest.getPermissions().size()) {
            throw new IllegalArgumentException(ErrorCode.PERMISSION_NOT_FOUND.getMessage());
        }
        role.getPermissions().addAll(permissions);
        return roleMapper.toRoleResponse(roleRepository.save(role));
    }

    @Override
    public RoleResponse update(Long id, RoleRequest roleRequest) {
        RoleEntity roleEntity = findByIdToEntity(id);
        if (roleRepository.findByRoleNameAndIsDeletedFalse(roleRequest.getRoleName()).isPresent()) {
            throw new ResourceConflictException(ErrorCode.ROLE_EXISTED.getMessage());
        }
        if (roleRequest.getPermissions() != null) {
            var permissions = permissionRepository.findAllById(roleRequest.getPermissions());
            if (permissions.size() != roleRequest.getPermissions().size()) {
                throw new ResourceNotFoundException(ErrorCode.PERMISSION_NOT_FOUND.getMessage());
            }
            roleEntity.setPermissions(new HashSet<>(permissions));
        }
        roleMapper.updateRole(roleRequest,roleEntity);
        return roleMapper.toRoleResponse(roleRepository.save(roleEntity));
    }

    @Override
    public void update(Long roleID, Long permissionID) {
        PermissionEntity permission = permissionService.findEntityById(permissionID);
        RoleEntity role = findByIdToEntity(roleID);

        if(role.getPermissions().contains(permission)) {
            role.getPermissions().remove(permission);
        } else {
            role.getPermissions().add(permission);
        }

        roleRepository.save(role);
    }

    @Override
    public void softDelete(Long id) {
        RoleEntity role = findByIdToEntity(id);
        role.setDeleted(true);
        roleRepository.save(role);
    }

    @Override
    public void restore(Long id) {
        RoleEntity role = findByIdToEntity(id);
        role.setDeleted(false);
        roleRepository.save(role);
    }

    @Override
    public RoleResponse findByIdToResponse(Long id) {
        return roleMapper.toRoleResponse(findByIdToEntity(id));
    }

    @Override
    public RoleEntity findByIdToEntity(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ROLE_NOT_FOUND.getMessage()));
    }

    @Override
    public RoleEntity findByRoleName(String roleName) {
        return roleRepository.findByRoleName(PredefinedRole.ROLE_STUDENT)
                .orElseThrow(() -> new ResourceNotFoundException(ROLE_NOT_FOUND.getMessage()));
    }



}
