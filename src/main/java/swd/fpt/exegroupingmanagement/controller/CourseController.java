package swd.fpt.exegroupingmanagement.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
import swd.fpt.exegroupingmanagement.dto.request.CourseRequest;
import swd.fpt.exegroupingmanagement.dto.response.CourseResponse;
import swd.fpt.exegroupingmanagement.dto.response.StandardResponse;
import static swd.fpt.exegroupingmanagement.dto.response.StandardResponse.success;
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
    @PreAuthorize("hasAnyAuthority('ADMIN','MENTOR')")
    @Operation(summary = "Create a new course")
    public ResponseEntity<StandardResponse<Object>> create(@Valid @RequestBody CourseRequest request) {
        CourseResponse result = courseService.create(request);
        return ResponseEntity.ok(success("Tạo lớp học thành công", result));
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    @Operation(summary = "Get course by ID")
    public ResponseEntity<StandardResponse<Object>> getById(@PathVariable Long id) {
        CourseResponse result = courseService.getById(id);
        return ResponseEntity.ok(success("Lấy thông tin lớp học thành công", result));
    }

    @GetMapping("/code/{code}")
    @PreAuthorize("permitAll()")
    @Operation(summary = "Get course by code")
    public ResponseEntity<StandardResponse<Object>> getByCode(@PathVariable String code) {
        CourseResponse result = courseService.getByCode(code);
        return ResponseEntity.ok(success("Lấy thông tin lớp học thành công", result));
    }

    @GetMapping
    @PreAuthorize("permitAll()")
    @Operation(summary = "Get all courses")
    public ResponseEntity<StandardResponse<Object>> getAll() {
        List<CourseResponse> result = courseService.getAll();
        return ResponseEntity.ok(success("Lấy danh sách lớp học thành công", result));
    }

    @GetMapping("/search")
    @PreAuthorize("permitAll()")
    @Operation(summary = "Search courses with multiple filters", 
               description = "Search courses by keyword (code or name), status, semester, mentor, and subject")
    public ResponseEntity<StandardResponse<Object>> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) CourseStatus status,
            @RequestParam(required = false) Long semesterId,
            @RequestParam(required = false) Long mentorId,
            @RequestParam(required = false) Long subjectId) {
        
        List<CourseResponse> result = courseService.searchCourses(keyword, status, semesterId, mentorId, subjectId);
        return ResponseEntity.ok(success(
                "Tìm kiếm lớp học thành công (tìm thấy " + result.size() + " kết quả)", 
                result));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("permitAll()")
    @Operation(summary = "Get courses by status")
    public ResponseEntity<StandardResponse<Object>> getByStatus(@PathVariable CourseStatus status) {
        List<CourseResponse> result = courseService.getByStatus(status);
        return ResponseEntity.ok(success("Lấy danh sách lớp học theo trạng thái thành công", result));
    }

    @GetMapping("/semester/{semesterId}")
    @PreAuthorize("permitAll()")
    @Operation(summary = "Get courses by semester")
    public ResponseEntity<StandardResponse<Object>> getBySemester(@PathVariable Long semesterId) {
        List<CourseResponse> result = courseService.getBySemester(semesterId);
        return ResponseEntity.ok(success("Lấy danh sách lớp học theo kỳ học thành công", result));
    }

    @GetMapping("/mentor/{mentorId}")
    @PreAuthorize("permitAll()")
    @Operation(summary = "Get courses by mentor")
    public ResponseEntity<StandardResponse<Object>> getByMentor(@PathVariable Long mentorId) {
        List<CourseResponse> result = courseService.getByMentor(mentorId);
        return ResponseEntity.ok(success("Lấy danh sách lớp học theo mentor thành công", result));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MENTOR')")
    @Operation(summary = "Update course")
    public ResponseEntity<StandardResponse<Object>> update(
            @PathVariable Long id,
            @Valid @RequestBody CourseRequest request) {
        CourseResponse result = courseService.update(id, request);
        return ResponseEntity.ok(success("Cập nhật lớp học thành công", result));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Delete course")
    public ResponseEntity<StandardResponse<String>> delete(@PathVariable Long id) {
        courseService.delete(id);
        return ResponseEntity.ok(success("Xóa lớp học thành công"));
    }
}
