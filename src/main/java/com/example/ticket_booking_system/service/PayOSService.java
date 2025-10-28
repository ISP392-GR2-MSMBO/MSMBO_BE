package com.example.ticket_booking_system.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.payos.PayOS;
import vn.payos.model.webhooks.Webhook;
import vn.payos.model.webhooks.WebhookData;


@Service
@RequiredArgsConstructor
public class PayOSService {

    private final PayOS payOS; // cấu hình qua @Bean hoặc @ConfigurationProperties

//    public CheckoutResponseData createPaymentLink(PaymentData data) throws Exception {
//        return payOS.createPaymentLink(data);
//    }

    public WebhookData verifyPaymentWebhookData(Webhook body) throws Exception {
        return payOS.webhooks().verify(body); // SDK 2.x
    }
}
