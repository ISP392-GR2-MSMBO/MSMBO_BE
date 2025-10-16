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

    @Column(nullable = false)
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
//    private boolean isDelete;
}
