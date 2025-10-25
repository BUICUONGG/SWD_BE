package swd.fpt.exegroupingmanagement.service;

import swd.fpt.exegroupingmanagement.dto.request.CreateCourseRequest;
import swd.fpt.exegroupingmanagement.dto.response.CourseResponse;

import java.util.List;

public interface CourseService {
    
    CourseResponse createCourse(CreateCourseRequest request);
    
    CourseResponse getCourseById(Long courseId);
    
    List<CourseResponse> getAllCourses();
    
    List<CourseResponse> getCoursesByMentor(Long mentorId);
    
    List<CourseResponse> getCoursesBySemester(Long semesterId);
    
    CourseResponse assignMentor(Long courseId, Long mentorId);
    
    void deleteCourse(Long courseId);
}

