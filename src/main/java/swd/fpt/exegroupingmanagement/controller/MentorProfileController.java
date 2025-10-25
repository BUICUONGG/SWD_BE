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
import swd.fpt.exegroupingmanagement.dto.request.MentorProfileRequest;
import swd.fpt.exegroupingmanagement.dto.response.ApiResponse;
import swd.fpt.exegroupingmanagement.dto.response.MentorProfileResponse;
import swd.fpt.exegroupingmanagement.service.MentorProfileService;

@RestController
@RequestMapping("/api/mentor-profiles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Mentor Profile Management", description = "APIs for managing mentor profiles")
public class MentorProfileController {
    MentorProfileService mentorProfileService;

    @PostMapping
    @Operation(summary = "Create a new mentor profile")
    public ApiResponse<MentorProfileResponse> create(@Valid @RequestBody MentorProfileRequest request) {
        return ApiResponse.<MentorProfileResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Tạo hồ sơ giảng viên thành công")
                .result(mentorProfileService.create(request))
                .build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get mentor profile by ID")
    public ApiResponse<MentorProfileResponse> getById(@PathVariable Long id) {
        return ApiResponse.<MentorProfileResponse>builder()
                .code(HttpStatus.OK.value())
                .result(mentorProfileService.getById(id))
                .build();
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get mentor profile by user ID")
    public ApiResponse<MentorProfileResponse> getByUserId(@PathVariable Long userId) {
        return ApiResponse.<MentorProfileResponse>builder()
                .code(HttpStatus.OK.value())
                .result(mentorProfileService.getByUserId(userId))
                .build();
    }

    @GetMapping("/employee-code/{employeeCode}")
    @Operation(summary = "Get mentor profile by employee code")
    public ApiResponse<MentorProfileResponse> getByEmployeeCode(@PathVariable String employeeCode) {
        return ApiResponse.<MentorProfileResponse>builder()
                .code(HttpStatus.OK.value())
                .result(mentorProfileService.getByEmployeeCode(employeeCode))
                .build();
    }

    @GetMapping
    @Operation(summary = "Get all mentor profiles")
    public ApiResponse<List<MentorProfileResponse>> getAll() {
        return ApiResponse.<List<MentorProfileResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(mentorProfileService.getAll())
                .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update mentor profile")
    public ApiResponse<MentorProfileResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody MentorProfileRequest request) {
        return ApiResponse.<MentorProfileResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Cập nhật hồ sơ giảng viên thành công")
                .result(mentorProfileService.update(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete mentor profile")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        mentorProfileService.delete(id);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Xóa hồ sơ giảng viên thành công")
                .build();
    }
}

