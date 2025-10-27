package com.example.ticket_booking_system.dto.reponse.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfirmPaymentResponse {

    private boolean success;
    private String message;
    private Long bookingId;
}
