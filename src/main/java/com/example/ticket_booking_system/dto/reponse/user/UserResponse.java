package com.example.ticket_booking_system.dto.reponse.user;

import com.example.ticket_booking_system.Enum.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long userID;
    private String userName;
    private String fullName;
    private String email;
    private String phone;
    private Role role;
    private boolean status;
}
