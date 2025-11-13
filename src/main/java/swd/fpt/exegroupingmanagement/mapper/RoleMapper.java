package swd.fpt.exegroupingmanagement.mapper;


import swd.fpt.exegroupingmanagement.dto.request.RoleRequest;
import swd.fpt.exegroupingmanagement.dto.response.RoleResponse;
import swd.fpt.exegroupingmanagement.entity.RoleEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleEntity toRole(RoleRequest roleRequest);

    @Mapping(target = "isDeleted", source = "deleted")
    RoleResponse toRoleResponse(RoleEntity role);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE )
    void updateRole(RoleRequest roleRequest, @MappingTarget RoleEntity role);
}
