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
import swd.fpt.exegroupingmanagement.dto.request.SemesterRequest;
import swd.fpt.exegroupingmanagement.dto.response.ApiResponse;
import swd.fpt.exegroupingmanagement.dto.response.SemesterResponse;
import swd.fpt.exegroupingmanagement.service.SemesterService;

@RestController
@RequestMapping("/api/semesters")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Semester Management", description = "APIs for managing semesters")
public class SemesterController {
    SemesterService semesterService;

    @PostMapping
    @Operation(summary = "Create a new semester")
    public ApiResponse<SemesterResponse> create(@Valid @RequestBody SemesterRequest request) {
        return ApiResponse.<SemesterResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Tạo kỳ học thành công")
                .result(semesterService.create(request))
                .build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get semester by ID")
    public ApiResponse<SemesterResponse> getById(@PathVariable Long id) {
        return ApiResponse.<SemesterResponse>builder()
                .code(HttpStatus.OK.value())
                .result(semesterService.getById(id))
                .build();
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "Get semester by code")
    public ApiResponse<SemesterResponse> getByCode(@PathVariable String code) {
        return ApiResponse.<SemesterResponse>builder()
                .code(HttpStatus.OK.value())
                .result(semesterService.getByCode(code))
                .build();
    }

    @GetMapping
    @Operation(summary = "Get all semesters")
    public ApiResponse<List<SemesterResponse>> getAll() {
        return ApiResponse.<List<SemesterResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(semesterService.getAll())
                .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update semester")
    public ApiResponse<SemesterResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody SemesterRequest request) {
        return ApiResponse.<SemesterResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Cập nhật kỳ học thành công")
                .result(semesterService.update(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete semester")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        semesterService.delete(id);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Xóa kỳ học thành công")
                .build();
    }
}

