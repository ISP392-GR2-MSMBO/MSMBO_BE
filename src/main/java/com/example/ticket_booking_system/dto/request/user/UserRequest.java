package com.example.ticket_booking_system.dto.request.user;

import com.example.ticket_booking_system.Enum.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UserRequest {
    @NotBlank(message = "Username cannot be blank")
    private String userName;

    @NotBlank(message = "Full name cannot be blank")
    private String fullName;

    @NotBlank(message = "Password cannot be blank")
    private String password;

    @NotBlank(message = "Email cannot be blank")
    private String email;

    @NotBlank(message = "Phone cannot be blank")
    private String phone;
    private Role role;  // AD, MA, ST, CUS
    private boolean status;
}
