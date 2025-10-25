package swd.fpt.exegroupingmanagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import swd.fpt.exegroupingmanagement.dto.response.CourseResponse;
import swd.fpt.exegroupingmanagement.dto.response.MentorDetailResponse;
import swd.fpt.exegroupingmanagement.dto.response.StandardResponse;
import swd.fpt.exegroupingmanagement.entity.UserEntity;
import swd.fpt.exegroupingmanagement.service.CourseService;
import swd.fpt.exegroupingmanagement.service.MentorService;

import java.util.List;

@RestController
@RequestMapping("/api/mentor")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Mentor", description = "Endpoints for mentor operations")
@PreAuthorize("hasRole('MENTOR')")
public class MentorController {
    
    private final MentorService mentorService;
    private final CourseService courseService;
    
    @GetMapping("/profile")
    @Operation(summary = "Get mentor profile")
    public ResponseEntity<StandardResponse<MentorDetailResponse>> getMyProfile(
            Authentication authentication) {
        
        UserEntity user = (UserEntity) authentication.getPrincipal();
        Long userId = user.getUserId();
        
        MentorDetailResponse response = mentorService.getMentorByUserId(userId);
        
        return ResponseEntity.ok(StandardResponse.<MentorDetailResponse>builder()
            .success(true)
            .data(response)
            .build());
    }
    
    @GetMapping("/courses")
    @Operation(summary = "Get my courses")
    public ResponseEntity<StandardResponse<List<CourseResponse>>> getMyCourses(
            Authentication authentication) {
        
        UserEntity user = (UserEntity) authentication.getPrincipal();
        Long userId = user.getUserId();
        
        log.info("Mentor {} getting courses", userId);
        
        List<CourseResponse> response = courseService.getCoursesByMentor(userId);
        
        return ResponseEntity.ok(StandardResponse.<List<CourseResponse>>builder()
            .success(true)
            .data(response)
            .build());
    }
    
    @GetMapping("/courses/{courseId}")
    @Operation(summary = "Get course details")
    public ResponseEntity<StandardResponse<CourseResponse>> getCourseDetail(
            @PathVariable Long courseId,
            Authentication authentication) {
        
        UserEntity user = (UserEntity) authentication.getPrincipal();
        Long userId = user.getUserId();
        
        CourseResponse course = courseService.getCourseById(courseId);
        
        // Verify mentor owns this course
        if (!course.getMentorId().equals(userId)) {
            return ResponseEntity.status(403)
                .body(StandardResponse.<CourseResponse>builder()
                    .success(false)
                    .message("You are not the mentor of this course")
                    .build());
        }
        
        return ResponseEntity.ok(StandardResponse.<CourseResponse>builder()
            .success(true)
            .data(course)
            .build());
    }
}

