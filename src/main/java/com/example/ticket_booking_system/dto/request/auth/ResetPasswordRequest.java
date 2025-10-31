// TẠO FILE MỚI:
// src/main/java/com/example/ticket_booking_system/dto/request/auth/ResetPasswordRequest.java
package com.example.ticket_booking_system.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetPasswordRequest {

    @NotBlank(message = "Token cannot be blank")
    private String token;

    @NotBlank(message = "New password cannot be blank")
    private String newPassword;
}
