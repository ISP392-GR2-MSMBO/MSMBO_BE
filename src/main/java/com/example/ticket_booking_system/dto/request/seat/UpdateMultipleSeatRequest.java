package com.example.ticket_booking_system.dto.request.seat;


import com.example.ticket_booking_system.Enum.SeatStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateMultipleSeatRequest {

    @NotEmpty(message = "List of update seat can not empty")
    private List<Long> seatIDs;

    @NotNull(message = "Seats status can not null")
    private SeatStatus newStatus;
}
