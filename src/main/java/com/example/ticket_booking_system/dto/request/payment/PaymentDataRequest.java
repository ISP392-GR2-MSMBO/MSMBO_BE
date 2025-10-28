// src/main/java/com/example/ticket_booking_system/dto/request/payment/PaymentDataRequest.java
package com.example.ticket_booking_system.dto.request.payment;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class PaymentDataRequest {
    private long orderCode;
    private BigDecimal amount; // Sử dụng BigDecimal
    private String description;
    private String cancelUrl;
    private String returnUrl;
}
