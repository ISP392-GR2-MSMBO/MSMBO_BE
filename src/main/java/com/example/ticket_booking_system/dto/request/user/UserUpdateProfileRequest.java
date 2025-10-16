package com.example.ticket_booking_system.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter

public class UserUpdateProfileRequest {
    @NotBlank(message = "Full name cannot be blank")
    private String fullName;
    @NotBlank(message = "Email cannot be blank")
    private String email;
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be exactly 10 digits")
    private String phone;
}
