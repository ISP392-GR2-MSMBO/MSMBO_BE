package com.example.ticket_booking_system.dto.request.showtime;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShowtimeRequest {
    //private Long showtimeID;
    private String theaterID;
    private Long movieID;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
