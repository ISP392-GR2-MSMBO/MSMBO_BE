//package com.example.ticket_booking_system.service;
//
//import com.example.ticket_booking_system.dto.request.payment.PaymentDataRequest;
//import org.springframework.stereotype.Service;
//import vn.payos.PayOS;
//import vn.payos.model.v2.paymentRequests.CreatePaymentLinkRequest;
//import vn.payos.model.v2.paymentRequests.CreatePaymentLinkResponse;
//import vn.payos.model.webhooks.WebhookData;
//
//@Service
//public class PayOSService {
//
//    private final PayOS payOS;
//
//    // Khuyên dùng: đọc từ ENV (PAYOS_CLIENT_ID / PAYOS_API_KEY / PAYOS_CHECKSUM_KEY)
//    public PayOSService() {
//        this.payOS = PayOS.fromEnv(); // hoặc new PayOS(ClientOptions.builder()...)
//    }
//
//    public String createPaymentLink(PaymentDataRequest request) throws Exception {
//        CreatePaymentLinkRequest data = CreatePaymentLinkRequest.builder()
//                .orderCode(request.getOrderCode()) // long
//                .amount(request.getAmount().longValue()) // v2 dùng Long
//                .description(request.getDescription())
//                .cancelUrl(request.getCancelUrl())
//                .returnUrl(request.getReturnUrl())
//                .build();
//
//        CreatePaymentLinkResponse res = payOS.paymentRequests().create(data);
//        return res.getCheckoutUrl();
//    }
//
//    public WebhookData verifyWebhookData(String body) throws Exception {
//        // V2: xác minh qua module webhooks, chỉ cần body
//        return payOS.webhooks().verify(body);
//    }
//}
