package com.example.ticket_booking_system.dto.request.user;

import com.example.ticket_booking_system.Enum.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {
    @NotBlank(message = "Username cannot be blank")
    private String userName;

    @NotBlank(message = "Full name cannot be blank")
    private String fullName;

    @NotBlank(message = "Password cannot be blank")
    private String password;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be exactly 10 digits")
    @NotBlank(message = "Phone cannot be blank")
    private String phone;
    private Role roleID;  // AD, MA, ST, CUS
    private boolean status;
}
