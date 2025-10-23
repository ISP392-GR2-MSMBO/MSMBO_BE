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
    //@NotBlank(message = "Email cannot be blank")
    private String email;

    @Pattern(regexp = "^0[0-9]{9}$", message = "Phone number must start at 0 and have 10 digit")
    //@NotBlank(message = "Phone cannot be blank")
    private String phone;
}
