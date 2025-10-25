package swd.fpt.exegroupingmanagement.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
import swd.fpt.exegroupingmanagement.dto.response.ApiResponse;
import swd.fpt.exegroupingmanagement.dto.response.EnrollmentResponse;
import swd.fpt.exegroupingmanagement.service.EnrollmentService;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Enrollment Management", description = "APIs for managing enrollments")
public class EnrollmentController {
    EnrollmentService enrollmentService;

    @PostMapping
    @Operation(summary = "Enroll a student in a course")
    public ApiResponse<EnrollmentResponse> enroll(@Valid @RequestBody EnrollmentRequest request) {
        return ApiResponse.<EnrollmentResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Đăng ký lớp thành công")
                .result(enrollmentService.enroll(request))
                .build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get enrollment by ID")
    public ApiResponse<EnrollmentResponse> getById(@PathVariable Long id) {
        return ApiResponse.<EnrollmentResponse>builder()
                .code(HttpStatus.OK.value())
                .result(enrollmentService.getById(id))
                .build();
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get enrollments by user")
    public ApiResponse<List<EnrollmentResponse>> getByUser(@PathVariable Long userId) {
        return ApiResponse.<List<EnrollmentResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(enrollmentService.getByUser(userId))
                .build();
    }

    @GetMapping("/course/{courseId}")
    @Operation(summary = "Get enrollments by course")
    public ApiResponse<List<EnrollmentResponse>> getByCourse(@PathVariable Long courseId) {
        return ApiResponse.<List<EnrollmentResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(enrollmentService.getByCourse(courseId))
                .build();
    }

    @PatchMapping("/{id}/approve")
    @Operation(summary = "Approve enrollment")
    public ApiResponse<EnrollmentResponse> approve(
            @PathVariable Long id,
            @RequestParam Long approvedBy) {
        return ApiResponse.<EnrollmentResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Phê duyệt đăng ký thành công")
                .result(enrollmentService.approveEnrollment(id, approvedBy))
                .build();
    }

    @PatchMapping("/{id}/complete")
    @Operation(summary = "Complete enrollment")
    public ApiResponse<EnrollmentResponse> complete(@PathVariable Long id) {
        return ApiResponse.<EnrollmentResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Hoàn thành đăng ký")
                .result(enrollmentService.completeEnrollment(id))
                .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete enrollment")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        enrollmentService.delete(id);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Hủy đăng ký thành công")
                .build();
    }
}

