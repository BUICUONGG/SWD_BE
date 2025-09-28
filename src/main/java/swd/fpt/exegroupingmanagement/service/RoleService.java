package swd.fpt.exegroupingmanagement.service;


import swd.fpt.exegroupingmanagement.dto.request.RoleRequest;
import swd.fpt.exegroupingmanagement.dto.response.RoleResponse;
import swd.fpt.exegroupingmanagement.entity.RoleEntity;

import java.util.List;

public interface RoleService {
    List<RoleResponse> getAllRoles();
    RoleResponse add(RoleRequest roleRequest);
    RoleResponse update(Long id,RoleRequest roleRequest);
    void update(Long roleID, Long permissionID);
    void softDelete(Long id);
    void restore(Long id);
    RoleResponse findByIdToResponse(Long id);
    RoleEntity findByIdToEntity(Long id);
    RoleEntity findByRoleName(String roleName);
}
