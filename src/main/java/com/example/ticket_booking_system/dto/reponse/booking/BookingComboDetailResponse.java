package com.example.ticket_booking_system.dto.reponse.booking;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookingComboDetailResponse {
    private String comboName;
    private Integer quantity;
    private Float unitPrice;
    private Float totalPrice; // (quantity * unitPrice)
}
