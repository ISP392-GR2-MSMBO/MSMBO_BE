package com.example.ticket_booking_system.dto.request.seat;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateSingleSeatRequest {

    @NotBlank(message = "Row name can not empty")
    private String rowName;

    @NotNull(message = "Seat number can not empty")
    private Integer number;

    @NotNull(message = "Seat type can not empty")
    private Long seatTypeId;
}
