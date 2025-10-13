package com.example.ticket_booking_system.dto.request.staff;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class StaffRequest {
    private Long staffID;
    private String department;
    private String position;
    private double salary;
}
