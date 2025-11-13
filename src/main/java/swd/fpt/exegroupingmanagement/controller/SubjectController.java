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
import swd.fpt.exegroupingmanagement.dto.request.SubjectRequest;
import swd.fpt.exegroupingmanagement.dto.response.StandardResponse;
import static swd.fpt.exegroupingmanagement.dto.response.StandardResponse.success;
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
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Create a new subject")
    public ResponseEntity<StandardResponse<Object>> create(@Valid @RequestBody SubjectRequest request) {
        SubjectResponse result = subjectService.create(request);
        return ResponseEntity.ok(success("Tạo môn học thành công", result));
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    @Operation(summary = "Get subject by ID")
    public ResponseEntity<StandardResponse<Object>> getById(@PathVariable Long id) {
        SubjectResponse result = subjectService.getById(id);
        return ResponseEntity.ok(success("Lấy thông tin môn học thành công", result));
    }

    @GetMapping("/code/{code}")
    @PreAuthorize("permitAll()")
    @Operation(summary = "Get subject by code")
    public ResponseEntity<StandardResponse<Object>> getByCode(@PathVariable String code) {
        SubjectResponse result = subjectService.getByCode(code);
        return ResponseEntity.ok(success("Lấy thông tin môn học thành công", result));
    }

    @GetMapping
    @PreAuthorize("permitAll()")
    @Operation(summary = "Get all subjects")
    public ResponseEntity<StandardResponse<Object>> getAll() {
        List<SubjectResponse> result = subjectService.getAll();
        return ResponseEntity.ok(success("Lấy danh sách môn học thành công", result));
    }

    @GetMapping("/search")
    @PreAuthorize("permitAll()")
    @Operation(summary = "Search subjects", 
               description = "Search subjects by keyword matching code or name")
    public ResponseEntity<StandardResponse<Object>> search(
            @RequestParam(required = false) String keyword) {
        
        List<SubjectResponse> result = subjectService.searchSubjects(keyword);
        return ResponseEntity.ok(success(
                "Tìm kiếm môn học thành công (tìm thấy " + result.size() + " kết quả)", 
                result));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Update subject")
    public ResponseEntity<StandardResponse<Object>> update(
            @PathVariable Long id,
            @Valid @RequestBody SubjectRequest request) {
        SubjectResponse result = subjectService.update(id, request);
        return ResponseEntity.ok(success("Cập nhật môn học thành công", result));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Delete subject")
    public ResponseEntity<StandardResponse<String>> delete(@PathVariable Long id) {
        subjectService.delete(id);
        return ResponseEntity.ok(success("Xóa môn học thành công"));
    }
}

