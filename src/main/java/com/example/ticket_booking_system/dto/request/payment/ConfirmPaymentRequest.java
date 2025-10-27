package com.example.ticket_booking_system.dto.request.payment;

import lombok.Data;

@Data
public class ConfirmPaymentRequest {

    private String code;
    private String id;
    private String status;
    private String orderCode;
    private Long amount;
    private String signature;
}
