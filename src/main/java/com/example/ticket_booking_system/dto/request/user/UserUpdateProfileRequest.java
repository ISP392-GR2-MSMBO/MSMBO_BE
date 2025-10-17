package com.example.ticket_booking_system.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UserUpdateProfileRequest {
    @NotBlank(message = "Full name cannot be blank")
    private String fullName;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be exactly 10 digits")
    @NotBlank(message = "Phone cannot be blank")
    private String phone;
}
