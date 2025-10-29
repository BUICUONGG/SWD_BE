package swd.fpt.exegroupingmanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import swd.fpt.exegroupingmanagement.dto.request.SemesterRequest;
import swd.fpt.exegroupingmanagement.dto.response.SemesterResponse;
import swd.fpt.exegroupingmanagement.entity.SemesterEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SemesterMapper {
    SemesterEntity toEntity(SemesterRequest request);
    SemesterResponse toResponse(SemesterEntity entity);
    List<SemesterResponse> toResponseList(List<SemesterEntity> entities);
    void updateEntity(@MappingTarget SemesterEntity entity, SemesterRequest request);
}

