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
import swd.fpt.exegroupingmanagement.dto.request.MajorRequest;
import swd.fpt.exegroupingmanagement.dto.response.MajorResponse;
import swd.fpt.exegroupingmanagement.dto.response.StandardResponse;
import static swd.fpt.exegroupingmanagement.dto.response.StandardResponse.success;
import swd.fpt.exegroupingmanagement.service.MajorService;

@RestController
@RequestMapping("/api/majors")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Major Management", description = "APIs for managing majors")
public class MajorController {
    MajorService majorService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Create a new major")
    public ResponseEntity<StandardResponse<Object>> create(@Valid @RequestBody MajorRequest request) {
        MajorResponse result = majorService.create(request);
        return ResponseEntity.ok(success("Tạo chuyên ngành thành công", result));
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    @Operation(summary = "Get major by ID")
    public ResponseEntity<StandardResponse<Object>> getById(@PathVariable Long id) {
        MajorResponse result = majorService.getById(id);
        return ResponseEntity.ok(success("Lấy thông tin chuyên ngành thành công", result));
    }

    @GetMapping("/code/{code}")
    @PreAuthorize("permitAll()")
    @Operation(summary = "Get major by code")
    public ResponseEntity<StandardResponse<Object>> getByCode(@PathVariable String code) {
        MajorResponse result = majorService.getByCode(code);
        return ResponseEntity.ok(success("Lấy thông tin chuyên ngành thành công", result));
    }

    @GetMapping
    @PreAuthorize("permitAll()")
    @Operation(summary = "Get all majors")
    public ResponseEntity<StandardResponse<Object>> getAll() {
        List<MajorResponse> result = majorService.getAll();
        return ResponseEntity.ok(success("Lấy danh sách chuyên ngành thành công", result));
    }

    @GetMapping("/search")
    @Operation(summary = "Search majors", 
               description = "Search majors by keyword matching code or name")
    public ResponseEntity<StandardResponse<Object>> search(
            @RequestParam(required = false) String keyword) {
        
        List<MajorResponse> result = majorService.searchMajors(keyword);
        return ResponseEntity.ok(success(
                "Tìm kiếm chuyên ngành thành công (tìm thấy " + result.size() + " kết quả)", 
                result));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Update major")
    public ResponseEntity<StandardResponse<Object>> update(
            @PathVariable Long id,
            @Valid @RequestBody MajorRequest request) {
        MajorResponse result = majorService.update(id, request);
        return ResponseEntity.ok(success("Cập nhật chuyên ngành thành công", result));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Delete major")
    public ResponseEntity<StandardResponse<String>> delete(@PathVariable Long id) {
        majorService.delete(id);
        return ResponseEntity.ok(success("Xóa chuyên ngành thành công"));
    }
}

