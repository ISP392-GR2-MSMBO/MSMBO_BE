package com.example.ticket_booking_system.dto.reponse.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentLinkResponseDTO {
    private String checkoutUrl;
}
