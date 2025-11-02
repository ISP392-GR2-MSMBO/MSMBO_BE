package com.example.ticket_booking_system.controller;

import com.example.ticket_booking_system.Enum.Role;
import com.example.ticket_booking_system.dto.request.user.ChangePasswordRequest;
import com.example.ticket_booking_system.dto.request.user.UserRequest;
import com.example.ticket_booking_system.dto.reponse.user.UserResponse;
import com.example.ticket_booking_system.dto.request.user.UserUpdateProfileRequest;
import com.example.ticket_booking_system.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:3000") // can them khi noi react
@CrossOrigin(origins = "https://chillcinema.vercel.app/") // cần thêm khi nối React
public class UserController {

    private final UserService userService;

    // Lấy danh sách user
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // Lấy user theo username
    @GetMapping("/userName")
    public ResponseEntity<List<UserResponse>> searchUsersByUsername(@RequestParam String keyword) {
        return ResponseEntity.ok(userService.searchUsersByUsername(keyword));
    }

    //search fullname
    @GetMapping("/fullName")
    public ResponseEntity<List<UserResponse>> searchUsers(@RequestParam String keyword) {
        return ResponseEntity.ok(userService.searchUsersByFullName(keyword));
    }

    // Tạo user mới
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest request) {
        return ResponseEntity.ok(userService.createUser(request));
    }

    // Cập nhật thông tin user theo ID
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id,@Valid @RequestBody UserUpdateProfileRequest request) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    // Xóa user theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    // doi role nguoi dung
    @PutMapping("/{id}/role")
    public ResponseEntity<UserResponse> updateUserRole(
            @PathVariable Long id,
            @RequestParam Role newRole) {
        return ResponseEntity.ok(userService.updateUserRole(id, newRole));
    }

    // Xác thực email khi người dùng bấm link trong mail
    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {
        userService.verifyEmail(token);
        return ResponseEntity.ok("Email verified successfully!");
    }

    //  Gửi lại email xác thực (resend)
    @PostMapping("/{id}/resend-verify")
    public ResponseEntity<String> resendVerification(@PathVariable Long id) {
        userService.resendEmailVerify(id);
        return ResponseEntity.ok("Verification email has been resent!");
    }

    // (OPTIONAL) Kiểm tra trạng thái xác thực email
    @GetMapping("/{id}/email-status")
    public ResponseEntity<Boolean> getEmailStatus(@PathVariable Long id) {
        return ResponseEntity.ok(userService.isEmailVerified(id));
    }

    /**
     * API: Đổi mật khẩu
     */
    @PatchMapping("/{id}/change-password")
    public ResponseEntity<String> changePassword(
            @PathVariable Long id,
            @Valid @RequestBody ChangePasswordRequest request
    ) {
        userService.changePassword(id, request);
        return ResponseEntity.ok("Password changed successfully!");
    }
}

