package com.example.ticket_booking_system.controller;

import com.example.ticket_booking_system.dto.reponse.auth.LoginResponse;
import com.example.ticket_booking_system.dto.reponse.user.UserResponse;
import com.example.ticket_booking_system.dto.request.auth.ForgotPasswordRequest;
import com.example.ticket_booking_system.dto.request.auth.LoginRequest;
import com.example.ticket_booking_system.dto.request.auth.RegisterRequest;
import com.example.ticket_booking_system.dto.request.auth.ResetPasswordRequest;
import com.example.ticket_booking_system.service.AuthService;
import com.example.ticket_booking_system.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:3000")
@CrossOrigin(origins = "https://chillcinema.netlify.app/") // cần thêm khi nối React
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    // 🟢 Register
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    // 🟡 Login
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    /**
     * API MỚI: (Bước A) Quên mật khẩu
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        userService.requestPasswordReset(request);
        // Luôn trả về OK 200 để bảo mật
        return ResponseEntity.ok("If your email exists in our system, a password reset link has been sent.");
    }

    /**
     * API MỚI: (Bước B) Đặt lại mật khẩu
     */
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        userService.resetPassword(request);
        return ResponseEntity.ok("Password has been reset successfully.");
    }
}
