package com.example.ticket_booking_system.dto.reponse.booking;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookingDetailResponse {
    private String seatRow; // "G"
    private Integer seatNumber; // 4
    private Float price; // 130500
}
