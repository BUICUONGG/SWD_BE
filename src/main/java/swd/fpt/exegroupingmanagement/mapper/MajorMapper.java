package swd.fpt.exegroupingmanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import swd.fpt.exegroupingmanagement.dto.request.MajorRequest;
import swd.fpt.exegroupingmanagement.dto.response.MajorResponse;
import swd.fpt.exegroupingmanagement.entity.MajorEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MajorMapper {
    MajorEntity toEntity(MajorRequest request);
    MajorResponse toResponse(MajorEntity entity);
    List<MajorResponse> toResponseList(List<MajorEntity> entities);
    void updateEntity(@MappingTarget MajorEntity entity, MajorRequest request);
}

