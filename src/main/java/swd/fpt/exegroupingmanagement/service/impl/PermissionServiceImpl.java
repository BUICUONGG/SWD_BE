package swd.fpt.exegroupingmanagement.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import swd.fpt.exegroupingmanagement.constant.PredefinedRole;
import swd.fpt.exegroupingmanagement.dto.request.PermissionRequest;
import swd.fpt.exegroupingmanagement.dto.response.PermissionResponse;
import swd.fpt.exegroupingmanagement.entity.PermissionEntity;
import swd.fpt.exegroupingmanagement.entity.RoleEntity;
import swd.fpt.exegroupingmanagement.exception.ErrorCode;
import swd.fpt.exegroupingmanagement.exception.exceptions.ResourceNotFoundException;
import swd.fpt.exegroupingmanagement.mapper.PermissionMapper;
import swd.fpt.exegroupingmanagement.repository.PermissionRepository;
import swd.fpt.exegroupingmanagement.repository.RoleRepository;
import swd.fpt.exegroupingmanagement.service.PermissionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionServiceImpl implements PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;
    RoleRepository roleRepository;

    @Override
    public List<PermissionResponse> getAllPermissions() {
        List<PermissionEntity> permissions = permissionRepository.findAllByOrderByPermissionIdAsc();
        if(permissions.isEmpty()){
            throw new ResourceNotFoundException(ErrorCode.PERMISSION_NOT_FOUND.getMessage());
        }
        return permissions.stream().map(permissionMapper::toPermissionResponse).toList();
    }

    @Override
    public PermissionResponse getPermissionById(Long id) {
        return permissionMapper.toPermissionResponse(findEntityById(id));
    }

    @Override
    public PermissionResponse add(PermissionRequest permissionRequest) {
        if(permissionRepository.existsByPermissionName(permissionRequest.getPermissionName())){
            throw new ResourceNotFoundException(ErrorCode.PERMISSION_EXISTED.getMessage());
        }

        PermissionEntity permission = permissionMapper.toPermission(permissionRequest);
        permissionRepository.save(permission);
        log.info("in");
        RoleEntity adminRole = roleRepository.findByRoleNameAndIsDeletedFalse(PredefinedRole.ROLE_ADMIN).orElseThrow(
                () -> new ResourceNotFoundException(ErrorCode.ROLE_NOT_FOUND.getMessage()));
        adminRole.getPermissions().add(permission);
        log.info("in");
        roleRepository.save(adminRole);
        return permissionMapper.toPermissionResponse(permission);
    }

    @Override
    public PermissionResponse update(Long id,PermissionRequest permissionRequest) {
        PermissionEntity permission = findEntityById(id);
        permissionMapper.updatePermission(permissionRequest, permission);
        return permissionMapper.toPermissionResponse(permissionRepository.save(permission));
    }

    @Override
    public void softDelete(Long id) {
        PermissionEntity permission = findEntityById(id);
        permission.setDeleted(true);
        permissionRepository.save(permission);
    }

    @Override
    public PermissionEntity findEntityById(Long id) {
        return  permissionRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(ErrorCode.PERMISSION_NOT_FOUND.getMessage()));
    }

    @Override
    public void restore(Long id) {
        PermissionEntity entity = findEntityById(id);
        entity.setDeleted(false);
        permissionRepository.save(entity);
    }
}
