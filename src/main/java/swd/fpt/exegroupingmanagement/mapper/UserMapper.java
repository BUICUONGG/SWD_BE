package swd.fpt.exegroupingmanagement.mapper;

import swd.fpt.exegroupingmanagement.dto.request.UserRequest;
import swd.fpt.exegroupingmanagement.dto.response.UserResponse;
import swd.fpt.exegroupingmanagement.entity.UserEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserEntity toEntity(UserRequest accountRequest);
    UserResponse toEntityDTO(UserEntity userEntity);
    List<UserResponse> toEntityDTO(List<UserEntity> userEntities);


}
