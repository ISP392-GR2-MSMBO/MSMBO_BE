package com.example.ticket_booking_system.dto.request.auth;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
    @NotBlank(message = "Username cannot be blank")
    private String userName;

    @NotBlank(message = "Full name cannot be blank")
    private String fullName;

    @NotBlank(message = "Password cannot be blank")
    private String password;

    @Email(message = "Invalid email format")
    private String email;

    @Pattern(regexp = "^0[0-9]{9}$", message = "Phone number must be 10 digits")
    private String phone;
}
