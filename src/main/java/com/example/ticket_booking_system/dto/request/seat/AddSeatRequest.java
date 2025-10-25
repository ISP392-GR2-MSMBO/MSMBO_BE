package com.example.ticket_booking_system.dto.request.seat;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AddSeatRequest {

    @NotEmpty(message = "List of create can not be empty")
    @Valid
    private List<CreateSingleSeatRequest> seats;
}
