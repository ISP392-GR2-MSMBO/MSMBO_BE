package com.example.ticket_booking_system.dto.reponse.user;

import com.example.ticket_booking_system.Enum.Role;
import jakarta.persistence.Column;
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
    private Role roleID;
    private boolean status;
    private boolean emailVerified; // 🆕 thêm để biết user đã verify hay chưa
}
