package swd.fpt.exegroupingmanagement.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import swd.fpt.exegroupingmanagement.dto.response.EnrollmentResponse;
import swd.fpt.exegroupingmanagement.entity.EnrollmentEntity;

@Mapper(componentModel = "spring")
public interface EnrollmentMapper {
    
    @Mapping(target = "userId", source = "user.userId")
    @Mapping(target = "userEmail", source = "user.email")
    @Mapping(target = "userFullName", source = "user.fullName")
    @Mapping(target = "courseId", source = "course.courseId")
    @Mapping(target = "courseCode", source = "course.code")
    @Mapping(target = "courseName", source = "course.name")
    @Mapping(target = "isDeleted", source = "deleted")
    EnrollmentResponse toResponse(EnrollmentEntity entity);
    
    List<EnrollmentResponse> toResponseList(List<EnrollmentEntity> entities);
}

