package com.example.ticket_booking_system.dto.request.seat;

import com.example.ticket_booking_system.Enum.SeatStatus;
import com.example.ticket_booking_system.entity.Theater;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class SeatRequest {

    @NotNull
    private Long seatID;
    @NotNull
    private Long theaterId;
    private String row;
    private Integer number;
    private String type;
    private Float price;
    @NotNull(message = "Seat must have a status")
    private SeatStatus status;
}
