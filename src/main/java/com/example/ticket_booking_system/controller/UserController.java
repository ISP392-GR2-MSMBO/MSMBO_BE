package com.example.ticket_booking_system.controller;

import com.example.ticket_booking_system.Enum.Role;
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
@CrossOrigin(origins = "http://localhost:3000") // can them khi noi react
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
}

