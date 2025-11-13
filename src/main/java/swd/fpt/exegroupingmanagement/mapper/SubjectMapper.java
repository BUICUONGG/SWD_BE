package swd.fpt.exegroupingmanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import swd.fpt.exegroupingmanagement.dto.request.SubjectRequest;
import swd.fpt.exegroupingmanagement.dto.response.SubjectResponse;
import swd.fpt.exegroupingmanagement.entity.SubjectEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubjectMapper {
    SubjectEntity toEntity(SubjectRequest request);

    @Mapping(target = "isDeleted", source = "deleted")
    SubjectResponse toResponse(SubjectEntity entity);

    List<SubjectResponse> toResponseList(List<SubjectEntity> entities);
    void updateEntity(@MappingTarget SubjectEntity entity, SubjectRequest request);
}

