package swd.fpt.exegroupingmanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import swd.fpt.exegroupingmanagement.dto.request.MentorProfileRequest;
import swd.fpt.exegroupingmanagement.dto.response.MentorProfileResponse;
import swd.fpt.exegroupingmanagement.entity.MentorProfileEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MentorProfileMapper {
    
    @Mapping(target = "user", ignore = true)
    MentorProfileEntity toEntity(MentorProfileRequest request);
    
    @Mapping(target = "userId", source = "user.userId")
    @Mapping(target = "userEmail", source = "user.email")
    @Mapping(target = "userFullName", source = "user.fullName")
    MentorProfileResponse toResponse(MentorProfileEntity entity);
    
    List<MentorProfileResponse> toResponseList(List<MentorProfileEntity> entities);
    
    @Mapping(target = "user", ignore = true)
    void updateEntity(@MappingTarget MentorProfileEntity entity, MentorProfileRequest request);
}

