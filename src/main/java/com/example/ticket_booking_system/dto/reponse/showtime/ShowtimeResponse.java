package com.example.ticket_booking_system.dto.reponse.showtime;

import com.example.ticket_booking_system.Enum.Approve;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShowtimeResponse {
    private Long showtimeID;
    private Long theaterID;
    private Long movieID;
    private LocalDate date;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    @Schema(example = "10:00")
    private LocalTime startTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    @Schema(example = "12:00")
    private LocalTime endTime;
    private Approve approveStatus;
    private boolean isDeleted;
}
