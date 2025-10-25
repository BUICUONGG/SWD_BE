package swd.fpt.exegroupingmanagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import swd.fpt.exegroupingmanagement.dto.request.CreateCourseRequest;
import swd.fpt.exegroupingmanagement.dto.response.CourseResponse;
import swd.fpt.exegroupingmanagement.dto.response.StandardResponse;
import swd.fpt.exegroupingmanagement.service.CourseService;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Course Management", description = "Endpoints for managing courses")
public class CourseController {
    
    private final CourseService courseService;
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create new course (Admin only)")
    public ResponseEntity<StandardResponse<CourseResponse>> createCourse(
            @Valid @RequestBody CreateCourseRequest request) {
        
        log.info("Creating course: {}", request.getCode());
        
        CourseResponse response = courseService.createCourse(request);
        
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(StandardResponse.<CourseResponse>builder()
                .success(true)
                .message("Course created successfully")
                .data(response)
                .build());
    }
    
    @GetMapping("/{courseId}")
    @Operation(summary = "Get course details")
    public ResponseEntity<StandardResponse<CourseResponse>> getCourse(
            @PathVariable Long courseId) {
        
        CourseResponse response = courseService.getCourseById(courseId);
        
        return ResponseEntity.ok(StandardResponse.<CourseResponse>builder()
            .success(true)
            .data(response)
            .build());
    }
    
    @GetMapping
    @Operation(summary = "Get all courses")
    public ResponseEntity<StandardResponse<List<CourseResponse>>> getAllCourses() {
        
        List<CourseResponse> response = courseService.getAllCourses();
        
        return ResponseEntity.ok(StandardResponse.<List<CourseResponse>>builder()
            .success(true)
            .data(response)
            .build());
    }
    
    @GetMapping("/semester/{semesterId}")
    @Operation(summary = "Get courses by semester")
    public ResponseEntity<StandardResponse<List<CourseResponse>>> getCoursesBySemester(
            @PathVariable Long semesterId) {
        
        List<CourseResponse> response = courseService.getCoursesBySemester(semesterId);
        
        return ResponseEntity.ok(StandardResponse.<List<CourseResponse>>builder()
            .success(true)
            .data(response)
            .build());
    }
    
    @PatchMapping("/{courseId}/assign-mentor")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Assign mentor to course (Admin only)")
    public ResponseEntity<StandardResponse<CourseResponse>> assignMentor(
            @PathVariable Long courseId,
            @RequestParam Long mentorId) {
        
        log.info("Assigning mentor {} to course {}", mentorId, courseId);
        
        CourseResponse response = courseService.assignMentor(courseId, mentorId);
        
        return ResponseEntity.ok(StandardResponse.<CourseResponse>builder()
            .success(true)
            .message("Mentor assigned successfully")
            .data(response)
            .build());
    }
    
    @DeleteMapping("/{courseId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete course (Admin only)")
    public ResponseEntity<StandardResponse<Void>> deleteCourse(@PathVariable Long courseId) {
        
        log.info("Deleting course: {}", courseId);
        
        courseService.deleteCourse(courseId);
        
        return ResponseEntity.ok(StandardResponse.<Void>builder()
            .success(true)
            .message("Course deleted successfully")
            .build());
    }
}

