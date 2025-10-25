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
import swd.fpt.exegroupingmanagement.dto.request.MajorRequest;
import swd.fpt.exegroupingmanagement.dto.response.ApiResponse;
import swd.fpt.exegroupingmanagement.dto.response.MajorResponse;
import swd.fpt.exegroupingmanagement.service.MajorService;

@RestController
@RequestMapping("/api/majors")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Major Management", description = "APIs for managing majors")
public class MajorController {
    MajorService majorService;

    @PostMapping
    @Operation(summary = "Create a new major")
    public ApiResponse<MajorResponse> create(@Valid @RequestBody MajorRequest request) {
        return ApiResponse.<MajorResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Tạo chuyên ngành thành công")
                .result(majorService.create(request))
                .build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get major by ID")
    public ApiResponse<MajorResponse> getById(@PathVariable Long id) {
        return ApiResponse.<MajorResponse>builder()
                .code(HttpStatus.OK.value())
                .result(majorService.getById(id))
                .build();
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "Get major by code")
    public ApiResponse<MajorResponse> getByCode(@PathVariable String code) {
        return ApiResponse.<MajorResponse>builder()
                .code(HttpStatus.OK.value())
                .result(majorService.getByCode(code))
                .build();
    }

    @GetMapping
    @Operation(summary = "Get all majors")
    public ApiResponse<List<MajorResponse>> getAll() {
        return ApiResponse.<List<MajorResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(majorService.getAll())
                .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update major")
    public ApiResponse<MajorResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody MajorRequest request) {
        return ApiResponse.<MajorResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Cập nhật chuyên ngành thành công")
                .result(majorService.update(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete major")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        majorService.delete(id);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Xóa chuyên ngành thành công")
                .build();
    }
}

