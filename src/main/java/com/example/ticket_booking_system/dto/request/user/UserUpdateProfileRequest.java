package com.example.ticket_booking_system.dto.request.user;

import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "Phone cannot be blank")
    private String phone;
}
