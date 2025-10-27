package com.example.ticket_booking_system.dto.request.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PaymentDataRequest {

    private Long orderCode;
    private Long amount;
    private String description;
    private String cancelUrl;
    private String returnUrl;
}
