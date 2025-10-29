package com.example.ticket_booking_system.dto.reponse.payment;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PaymentResponseDTO {

    private Long paymentID;
    private Long bookingID; // Lấy từ booking.bookingID
    private String status;
    private Float total;
    private String paymentMethod;
    private LocalDateTime paymentTime;
    private String gatewayTxnId;
}
