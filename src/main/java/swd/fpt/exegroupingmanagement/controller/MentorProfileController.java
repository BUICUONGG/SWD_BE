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
import swd.fpt.exegroupingmanagement.dto.request.MentorProfileRequest;
import swd.fpt.exegroupingmanagement.dto.response.MentorProfileResponse;
import swd.fpt.exegroupingmanagement.dto.response.StandardResponse;
import static swd.fpt.exegroupingmanagement.dto.response.StandardResponse.success;
import swd.fpt.exegroupingmanagement.service.MentorProfileService;

@RestController
@RequestMapping("/api/mentor-profiles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Mentor Profile Management", description = "APIs for managing mentor profiles")
public class MentorProfileController {
    MentorProfileService mentorProfileService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Create a new mentor profile")
    public ResponseEntity<StandardResponse<Object>> create(@Valid @RequestBody MentorProfileRequest request) {
        MentorProfileResponse result = mentorProfileService.create(request);
        return ResponseEntity.ok(success("Tạo hồ sơ giảng viên thành công", result));
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    @Operation(summary = "Get mentor profile by ID")
    public ResponseEntity<StandardResponse<Object>> getById(@PathVariable Long id) {
        MentorProfileResponse result = mentorProfileService.getById(id);
        return ResponseEntity.ok(success("Lấy thông tin hồ sơ giảng viên thành công", result));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("permitAll()")
    @Operation(summary = "Get mentor profile by user ID")
    public ResponseEntity<StandardResponse<Object>> getByUserId(@PathVariable Long userId) {
        MentorProfileResponse result = mentorProfileService.getByUserId(userId);
        return ResponseEntity.ok(success("Lấy thông tin hồ sơ giảng viên theo user thành công", result));
    }


    @GetMapping
    @PreAuthorize("permitAll()")
    @Operation(summary = "Get all mentor profiles")
    public ResponseEntity<StandardResponse<Object>> getAll() {
        List<MentorProfileResponse> result = mentorProfileService.getAll();
        return ResponseEntity.ok(success("Lấy danh sách hồ sơ giảng viên thành công", result));
    }

    @GetMapping("/search")
    @Operation(summary = "Search mentor profiles", 
               description = "Search mentor profiles by shortName or user's fullName")
    @PreAuthorize("permitAll()")
    public ResponseEntity<StandardResponse<Object>> search(
            @RequestParam(required = false) String keyword) {
        
        List<MentorProfileResponse> result = mentorProfileService.searchProfiles(keyword);
        return ResponseEntity.ok(success(
                "Tìm kiếm hồ sơ giảng viên thành công (tìm thấy " + result.size() + " kết quả)", 
                result));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Update mentor profile")
    public ResponseEntity<StandardResponse<Object>> update(
            @PathVariable Long id,
            @Valid @RequestBody MentorProfileRequest request) {
        MentorProfileResponse result = mentorProfileService.update(id, request);
        return ResponseEntity.ok(success("Cập nhật hồ sơ giảng viên thành công", result));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Delete mentor profile")
    public ResponseEntity<StandardResponse<String>> delete(@PathVariable Long id) {
        mentorProfileService.delete(id);
        return ResponseEntity.ok(success("Xóa hồ sơ giảng viên thành công"));
    }
}

