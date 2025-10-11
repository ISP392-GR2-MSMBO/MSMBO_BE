package com.example.ticket_booking_system.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tblShowtime", schema = "dbo")
public class Showtime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long showtimeID;
    private String theaterID;
    private Long movieID;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
