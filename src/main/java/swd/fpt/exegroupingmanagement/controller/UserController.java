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
import swd.fpt.exegroupingmanagement.dto.request.UserRequest;
import swd.fpt.exegroupingmanagement.dto.response.StandardResponse;
import static swd.fpt.exegroupingmanagement.dto.response.StandardResponse.success;
import swd.fpt.exegroupingmanagement.dto.response.UserResponse;
import swd.fpt.exegroupingmanagement.service.UserService;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "User Management", description = "APIs for managing users")
public class UserController {
    UserService userService;

    @PostMapping
    @Operation(summary = "Create new user (Admin only)")
    public ResponseEntity<StandardResponse<Object>> createUser(@Valid @RequestBody UserRequest request) {
        UserResponse result = userService.createUser(request);
        return ResponseEntity.ok(success("Tạo người dùng thành công", result));
    }

    @GetMapping("/me")
    @Operation(summary = "Get current user information")
    public ResponseEntity<StandardResponse<Object>> getMyInfo() {
        UserResponse result = userService.getMyInform();
        return ResponseEntity.ok(success("Lấy thông tin người dùng hiện tại thành công", result));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID")
    public ResponseEntity<StandardResponse<Object>> getUserById(@PathVariable Long id) {
        UserResponse result = userService.getUserResponseById(id);
        return ResponseEntity.ok(success("Lấy thông tin người dùng thành công", result));
    }

    @GetMapping
    @Operation(summary = "Get all users")
    public ResponseEntity<StandardResponse<Object>> getAllUsers() {
        List<UserResponse> result = userService.getAllUsers();
        return ResponseEntity.ok(success("Lấy danh sách người dùng thành công", result));
    }

    @GetMapping("/search")
    @Operation(summary = "Search users by email or fullName", 
               description = "Search users by keyword matching email or full name (case-insensitive)")
    public ResponseEntity<StandardResponse<Object>> searchUsers(
            @RequestParam(required = false) String keyword) {
        
        List<UserResponse> result = userService.searchUsers(keyword);
        return ResponseEntity.ok(success(
                "Tìm kiếm người dùng thành công (tìm thấy " + result.size() + " kết quả)", 
                result));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user (soft delete)")
    public ResponseEntity<StandardResponse<String>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(success("Xóa người dùng thành công"));
    }

    @PutMapping("/{id}/restore")
    @Operation(summary = "Restore deleted user")
    public ResponseEntity<StandardResponse<String>> restoreUser(@PathVariable Long id) {
        userService.restoreUser(id);
        return ResponseEntity.ok(success("Khôi phục người dùng thành công"));
    }
}

