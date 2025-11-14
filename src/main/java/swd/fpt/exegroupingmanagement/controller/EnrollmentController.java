package swd.fpt.exegroupingmanagement.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import swd.fpt.exegroupingmanagement.dto.request.EnrollmentRequest;
import swd.fpt.exegroupingmanagement.dto.response.EnrollmentResponse;
import swd.fpt.exegroupingmanagement.dto.response.StandardResponse;
import static swd.fpt.exegroupingmanagement.dto.response.StandardResponse.success;
import swd.fpt.exegroupingmanagement.service.EnrollmentService;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Enrollment Management", description = "APIs for managing enrollments")
public class EnrollmentController {
    EnrollmentService enrollmentService;

    @PostMapping
    @PreAuthorize("hasAuthority('STUDENT')")
    @Operation(summary = "Enroll a student in a course")
    public ResponseEntity<StandardResponse<Object>> enroll(@Valid @RequestBody EnrollmentRequest request) {
        EnrollmentResponse result = enrollmentService.enroll(request);
        return ResponseEntity.ok(success("Đăng ký lớp thành công", result));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MENTOR')")
    @Operation(summary = "Get enrollment by ID")
    public ResponseEntity<StandardResponse<Object>> getById(@PathVariable Long id) {
        EnrollmentResponse result = enrollmentService.getById(id);
        return ResponseEntity.ok(success("Lấy thông tin đăng ký thành công", result));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get enrollments by user")
    public ResponseEntity<StandardResponse<Object>> getByUser(@PathVariable Long userId) {
        List<EnrollmentResponse> result = enrollmentService.getByUser(userId);
        return ResponseEntity.ok(success("Lấy danh sách đăng ký theo người dùng thành công", result));
    }

    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MENTOR')")
    @Operation(summary = "Get enrollments by course")
    public ResponseEntity<StandardResponse<Object>> getByCourse(@PathVariable Long courseId) {
        List<EnrollmentResponse> result = enrollmentService.getByCourse(courseId);
        return ResponseEntity.ok(success("Lấy danh sách đăng ký theo lớp học thành công", result));
    }

    @GetMapping("/search")
    @Operation(summary = "Search enrollments", 
               description = "Search enrollments by userId, courseId, or both")
    @PreAuthorize("hasAnyAuthority('ADMIN','MENTOR')")
    public ResponseEntity<StandardResponse<Object>> search(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long courseId) {
        
        List<EnrollmentResponse> result = enrollmentService.searchEnrollments(userId, courseId);
        return ResponseEntity.ok(success(
                "Tìm kiếm đăng ký thành công (tìm thấy " + result.size() + " kết quả)", 
                result));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Delete enrollment")
    public ResponseEntity<StandardResponse<String>> delete(@PathVariable Long id) {
        enrollmentService.delete(id);
        return ResponseEntity.ok(success("Hủy đăng ký thành công"));
    }

    @DeleteMapping("/my-courses/{courseId}")
    @PreAuthorize("hasAuthority('STUDENT')")
    @Operation(summary = "Unenroll current student from course", description = "Sinh viên hủy đăng ký khóa học của chính mình")
    public ResponseEntity<StandardResponse<String>> unenrollCurrentUser(@PathVariable Long courseId) {
        enrollmentService.unenrollCurrentUser(courseId);
        return ResponseEntity.ok(success("Hủy đăng ký lớp thành công"));
    }
}

