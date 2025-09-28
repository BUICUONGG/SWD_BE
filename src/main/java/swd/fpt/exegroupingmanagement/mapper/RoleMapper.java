package swd.fpt.exegroupingmanagement.mapper;


import swd.fpt.exegroupingmanagement.dto.request.RoleRequest;
import swd.fpt.exegroupingmanagement.dto.response.RoleResponse;
import swd.fpt.exegroupingmanagement.entity.RoleEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    RoleEntity toRole(RoleRequest roleRequest);
    RoleResponse toRoleResponse(RoleEntity role);
    @Mapping(target = "permissions", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE )
    void updateRole(RoleRequest roleRequest, @MappingTarget RoleEntity role);
}
