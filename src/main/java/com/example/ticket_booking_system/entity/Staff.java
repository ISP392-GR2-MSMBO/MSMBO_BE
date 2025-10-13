package com.example.ticket_booking_system.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "tblEmployees", schema = "dbo")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long staffID;

    @OneToOne
    @JoinColumn(name = "userID", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private String position;

    @Column(nullable = false)
    private double salary;
}

