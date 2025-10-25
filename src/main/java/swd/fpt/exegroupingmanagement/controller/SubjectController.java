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
import swd.fpt.exegroupingmanagement.dto.request.SubjectRequest;
import swd.fpt.exegroupingmanagement.dto.response.ApiResponse;
import swd.fpt.exegroupingmanagement.dto.response.SubjectResponse;
import swd.fpt.exegroupingmanagement.service.SubjectService;

@RestController
@RequestMapping("/api/subjects")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Subject Management", description = "APIs for managing subjects")
public class SubjectController {
    SubjectService subjectService;

    @PostMapping
    @Operation(summary = "Create a new subject")
    public ApiResponse<SubjectResponse> create(@Valid @RequestBody SubjectRequest request) {
        return ApiResponse.<SubjectResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Tạo môn học thành công")
                .result(subjectService.create(request))
                .build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get subject by ID")
    public ApiResponse<SubjectResponse> getById(@PathVariable Long id) {
        return ApiResponse.<SubjectResponse>builder()
                .code(HttpStatus.OK.value())
                .result(subjectService.getById(id))
                .build();
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "Get subject by code")
    public ApiResponse<SubjectResponse> getByCode(@PathVariable String code) {
        return ApiResponse.<SubjectResponse>builder()
                .code(HttpStatus.OK.value())
                .result(subjectService.getByCode(code))
                .build();
    }

    @GetMapping
    @Operation(summary = "Get all subjects")
    public ApiResponse<List<SubjectResponse>> getAll() {
        return ApiResponse.<List<SubjectResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(subjectService.getAll())
                .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update subject")
    public ApiResponse<SubjectResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody SubjectRequest request) {
        return ApiResponse.<SubjectResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Cập nhật môn học thành công")
                .result(subjectService.update(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete subject")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        subjectService.delete(id);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Xóa môn học thành công")
                .build();
    }
}

