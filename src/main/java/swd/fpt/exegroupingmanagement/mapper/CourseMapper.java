package swd.fpt.exegroupingmanagement.mapper;

import org.mapstruct.*;
import swd.fpt.exegroupingmanagement.dto.request.CourseRequest;
import swd.fpt.exegroupingmanagement.dto.response.CourseResponse;
import swd.fpt.exegroupingmanagement.entity.CourseEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    
    @Mapping(target = "mentor", ignore = true)
    @Mapping(target = "subject", ignore = true)
    @Mapping(target = "semester", ignore = true)
    CourseEntity toEntity(CourseRequest request);
    
    @Mapping(target = "mentorId", source = "mentor.userId")
    @Mapping(target = "mentorName", source = "mentor.fullName")
    @Mapping(target = "subjectId", source = "subject.subjectId")
    @Mapping(target = "subjectCode", source = "subject.code")
    @Mapping(target = "semesterId", source = "semester.semesterId")
    @Mapping(target = "semesterCode", source = "semester.code")
    @Mapping(target = "isDeleted", source = "deleted")
    CourseResponse toResponse(CourseEntity entity);
    
    List<CourseResponse> toResponseList(List<CourseEntity> entities);
    
    @Mapping(target = "mentor", ignore = true)
    @Mapping(target = "subject", ignore = true)
    @Mapping(target = "semester", ignore = true)
    void updateEntity(@MappingTarget CourseEntity entity, CourseRequest request);
}

