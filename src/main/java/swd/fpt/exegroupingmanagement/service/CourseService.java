package swd.fpt.exegroupingmanagement.service;

import java.util.List;

import swd.fpt.exegroupingmanagement.dto.request.CourseRequest;
import swd.fpt.exegroupingmanagement.dto.response.CourseResponse;
import swd.fpt.exegroupingmanagement.enums.CourseStatus;

public interface CourseService {
    CourseResponse create(CourseRequest request);
    CourseResponse getById(Long id);
    CourseResponse getByCode(String code);
    List<CourseResponse> getAll();
    List<CourseResponse> getByStatus(CourseStatus status);
    List<CourseResponse> getBySemester(Long semesterId);
    List<CourseResponse> getByMentor(Long mentorId);
    CourseResponse update(Long id, CourseRequest request);
    void delete(Long id);
    
    // Search method
    List<CourseResponse> searchCourses(String keyword, CourseStatus status, Long semesterId, Long mentorId, Long subjectId);
}
