package swd.fpt.exegroupingmanagement.service;

import swd.fpt.exegroupingmanagement.dto.request.PermissionRequest;
import swd.fpt.exegroupingmanagement.dto.response.PermissionResponse;
import swd.fpt.exegroupingmanagement.entity.PermissionEntity;

import java.util.List;

public interface PermissionService {
    List<PermissionResponse> getAllPermissions();
    PermissionResponse getPermissionById(Long id);
    PermissionResponse add(PermissionRequest permissionRequest);
    PermissionResponse update(Long id,PermissionRequest permissionRequest);
    void softDelete(Long id);
    PermissionEntity findEntityById(Long id);
    void restore(Long id);
}
