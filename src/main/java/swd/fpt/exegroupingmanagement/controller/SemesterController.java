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
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import swd.fpt.exegroupingmanagement.dto.request.SemesterRequest;
import swd.fpt.exegroupingmanagement.dto.response.SemesterResponse;
import swd.fpt.exegroupingmanagement.dto.response.StandardResponse;
import static swd.fpt.exegroupingmanagement.dto.response.StandardResponse.success;
import swd.fpt.exegroupingmanagement.service.SemesterService;

@RestController
@RequestMapping("/api/semesters")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Semester Management", description = "APIs for managing semesters")
public class SemesterController {
    SemesterService semesterService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Create a new semester")
    public ResponseEntity<StandardResponse<Object>> create(@Valid @RequestBody SemesterRequest request) {
        SemesterResponse result = semesterService.create(request);
        return ResponseEntity.ok(success("Tạo kỳ học thành công", result));
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    @Operation(summary = "Get semester by ID")
    public ResponseEntity<StandardResponse<Object>> getById(@PathVariable Long id) {
        SemesterResponse result = semesterService.getById(id);
        return ResponseEntity.ok(success("Lấy thông tin kỳ học thành công", result));
    }

    @GetMapping("/code/{code}")
    @PreAuthorize("permitAll()")
    @Operation(summary = "Get semester by code")
    public ResponseEntity<StandardResponse<Object>> getByCode(@PathVariable String code) {
        SemesterResponse result = semesterService.getByCode(code);
        return ResponseEntity.ok(success("Lấy thông tin kỳ học thành công", result));
    }

    @GetMapping
    @PreAuthorize("permitAll()")
    @Operation(summary = "Get all semesters")
    public ResponseEntity<StandardResponse<Object>> getAll() {
        List<SemesterResponse> result = semesterService.getAll();
        return ResponseEntity.ok(success("Lấy danh sách kỳ học thành công", result));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Update semester")
    public ResponseEntity<StandardResponse<Object>> update(
            @PathVariable Long id,
            @Valid @RequestBody SemesterRequest request) {
        SemesterResponse result = semesterService.update(id, request);
        return ResponseEntity.ok(success("Cập nhật kỳ học thành công", result));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Delete semester")
    public ResponseEntity<StandardResponse<String>> delete(@PathVariable Long id) {
        semesterService.delete(id);
        return ResponseEntity.ok(success("Xóa kỳ học thành công"));
    }
}

