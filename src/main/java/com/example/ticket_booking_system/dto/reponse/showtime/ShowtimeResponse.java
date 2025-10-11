package com.example.ticket_booking_system.dto.reponse.showtime;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShowtimeResponse {
    private Long showtimeID;
    private String theaterID;
    private Long movieID;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
