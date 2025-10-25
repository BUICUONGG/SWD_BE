package swd.fpt.exegroupingmanagement.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import swd.fpt.exegroupingmanagement.dto.request.CourseRequest;
import swd.fpt.exegroupingmanagement.dto.response.ApiResponse;
import swd.fpt.exegroupingmanagement.dto.response.CourseResponse;
import swd.fpt.exegroupingmanagement.enums.CourseStatus;
import swd.fpt.exegroupingmanagement.service.CourseService;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Course Management", description = "APIs for managing courses")
public class CourseController {
    CourseService courseService;

    @PostMapping
    @Operation(summary = "Create a new course")
    public ApiResponse<CourseResponse> create(@Valid @RequestBody CourseRequest request) {
        return ApiResponse.<CourseResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Tạo lớp học thành công")
                .result(courseService.create(request))
                .build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get course by ID")
    public ApiResponse<CourseResponse> getById(@PathVariable Long id) {
        return ApiResponse.<CourseResponse>builder()
                .code(HttpStatus.OK.value())
                .result(courseService.getById(id))
                .build();
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "Get course by code")
    public ApiResponse<CourseResponse> getByCode(@PathVariable String code) {
        return ApiResponse.<CourseResponse>builder()
                .code(HttpStatus.OK.value())
                .result(courseService.getByCode(code))
                .build();
    }

    @GetMapping
    @Operation(summary = "Get all courses")
    public ApiResponse<List<CourseResponse>> getAll() {
        return ApiResponse.<List<CourseResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(courseService.getAll())
                .build();
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get courses by status")
    public ApiResponse<List<CourseResponse>> getByStatus(@PathVariable CourseStatus status) {
        return ApiResponse.<List<CourseResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(courseService.getByStatus(status))
                .build();
    }

    @GetMapping("/semester/{semesterId}")
    @Operation(summary = "Get courses by semester")
    public ApiResponse<List<CourseResponse>> getBySemester(@PathVariable Long semesterId) {
        return ApiResponse.<List<CourseResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(courseService.getBySemester(semesterId))
                .build();
    }

    @GetMapping("/mentor/{mentorId}")
    @Operation(summary = "Get courses by mentor")
    public ApiResponse<List<CourseResponse>> getByMentor(@PathVariable Long mentorId) {
        return ApiResponse.<List<CourseResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(courseService.getByMentor(mentorId))
                .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update course")
    public ApiResponse<CourseResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody CourseRequest request) {
        return ApiResponse.<CourseResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Cập nhật lớp học thành công")
                .result(courseService.update(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete course")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        courseService.delete(id);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Xóa lớp học thành công")
                .build();
    }
}
