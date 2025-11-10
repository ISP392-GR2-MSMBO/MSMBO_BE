package com.example.ticket_booking_system.entity;

import com.example.ticket_booking_system.Enum.Role;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "tblUsers", schema = "dbo")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long userID;

    @Column(nullable = false, unique = true)
    private String userName;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String fullName;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String phone;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role roleID; // AD, MA, ST, CUS

    @Column(nullable = false)
    private boolean status = true; // true = active, false = inactive

    @Builder.Default
    @Column(name = "is_deleted", nullable = false)
    private boolean isDelete=false;
    // ✅ Thêm trường xác thực email
    @Column(nullable = false)
    private boolean emailVerified = false;
    @Column(length = 64)
    private String emailVerifyToken;      // token ngắn
    private java.time.LocalDateTime emailVerifyTokenExp; // hạn token (vd: +24h)
}
