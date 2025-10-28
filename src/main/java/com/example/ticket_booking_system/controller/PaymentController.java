package com.example.ticket_booking_system.controller;

import com.example.ticket_booking_system.dto.request.payment.PaymentDataRequest;
import com.example.ticket_booking_system.dto.request.payment.ApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.payos.PayOS;
import vn.payos.model.webhooks.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    private final PayOS payOS;

    public PaymentController(PayOS payOS) {
        super();
        this.payOS = payOS;
    }

    @PostMapping(path = "/payos_transfer_handler")
    public ApiResponse<WebhookData> payosTransferHandler(@RequestBody PaymentDataRequest request)
            throws JsonProcessingException, IllegalArgumentException {
        try {
            WebhookData data = payOS.webhooks().verify(request);
            System.out.println("Payment data");
            return ApiResponse.success("Webhook delivered", data);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("payment error");
            return ApiResponse.error(e.getMessage());
        }
    }
}
