package com.example.ticket_booking_system.dto.reponse.auth;

import com.example.ticket_booking_system.Enum.Role;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    private String token;
    private Long userID;
    private String userName;
    private Role roleID;
}
