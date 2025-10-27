package com.example.ticket_booking_system.dto.request.payment;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreatePaymentRequest {

    @NotNull
    private Long bookingId;
}
