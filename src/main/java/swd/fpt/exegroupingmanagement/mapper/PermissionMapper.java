package swd.fpt.exegroupingmanagement.mapper;


import swd.fpt.exegroupingmanagement.dto.request.PermissionRequest;
import swd.fpt.exegroupingmanagement.dto.response.PermissionResponse;
import swd.fpt.exegroupingmanagement.entity.PermissionEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    PermissionEntity toPermission(PermissionRequest permissionRequest);
    PermissionResponse toPermissionResponse(PermissionEntity permission);
    @BeanMapping(nullValuePropertyMappingStrategy =  NullValuePropertyMappingStrategy.IGNORE)
    void updatePermission(PermissionRequest permissionRequest, @MappingTarget PermissionEntity permissionEntity);
}
