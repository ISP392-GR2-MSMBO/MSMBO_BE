//package com.example.ticket_booking_system.service;
//
//import com.example.ticket_booking_system.Enum.BookingStatus;
//import com.example.ticket_booking_system.Enum.PaymentStatus;
//import com.example.ticket_booking_system.dto.reponse.payment.CreatePaymentResponse;
//import com.example.ticket_booking_system.dto.request.payment.CreatePaymentRequest;
//import com.example.ticket_booking_system.dto.request.payment.PaymentDataRequest;
//import com.example.ticket_booking_system.entity.Booking;
//import com.example.ticket_booking_system.entity.Payment;
//import com.example.ticket_booking_system.repository.BookingRepository;
//import com.example.ticket_booking_system.repository.PaymentRepository;
//
//import jakarta.persistence.EntityNotFoundException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//// Đảm bảo import đúng 'WebhookData'
//import vn.payos.model.v2.webhooks.WebhookData;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//
//@Service
//public class PaymentService {
//    @Autowired private BookingRepository bookingRepository;
//    @Autowired private PaymentRepository paymentRepository;
//    @Autowired private PayOSService payOSService;
//
//    @Value("${payos.return-url}") private String payOsReturnUrl;
//    @Value("${payos.cancel-url:http://localhost:3000/payment-cancel}")
//    private String payOsCancelUrl;
//
//    @Transactional
//    public CreatePaymentResponse createPaymentLink(CreatePaymentRequest request) {
//        Booking booking = bookingRepository.findById(request.getBookingId())
//                .orElseThrow(() -> new EntityNotFoundException("Booking not found: " + request.getBookingId()));
//
//        if (booking.getStatus() != BookingStatus.PENDING) {
//            throw new IllegalStateException("Booking " + request.getBookingId() + " is not PENDING");
//        }
//
//        // Chuyển đổi Float (từ booking) sang BigDecimal (cho payment)
//        BigDecimal totalPrice = new BigDecimal(booking.getTotalPrice().toString());
//
//        Payment payment = Payment.builder()
//                .booking(booking)
//                .total(totalPrice)         // Đã dùng BigDecimal
//                .status(PaymentStatus.PENDING)
//                .paymentMethod("PAYOS")
//                .build();
//        Payment savedPayment = paymentRepository.save(payment);
//
//        PaymentDataRequest dto = PaymentDataRequest.builder()
//                // Dùng 'getPaymentId()' (viết thường chữ 'd')
//                .orderCode(savedPayment.getPaymentId()) // Long
//                .amount(savedPayment.getTotal().longValue()) // Chuyển BigDecimal sang Long (tiền đồng)
//                .description("Thanh toan ve phim booking " + booking.getBookingID())
//                .returnUrl(payOsReturnUrl)
//                .cancelUrl(payOsCancelUrl)
//                .build();
//
//        try {
//            String checkoutUrl = payOSService.createPaymentLink(dto);
//            return new CreatePaymentResponse(checkoutUrl);
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to create payment link: " + e.getMessage(), e);
//        }
//    }
//
//    @Transactional
//    public void handleWebhook(String webhookJsonBody) throws Exception {
//        // 1) Xác thực chữ ký + parse payload (SDK v2)
//        WebhookData webhook = payOSService.verifyWebhookData(webhookJsonBody);
//
//        if (webhook == null || !Boolean.TRUE.equals(webhook.getSuccess())) {
//            throw new IllegalArgumentException("Webhook not successful");
//        }
//
//        var data = webhook.getData();
//        if (data == null) throw new IllegalArgumentException("Webhook data is null");
//
//        Long paymentId = data.getOrderCode();
//        Payment payment = paymentRepository.findById(paymentId)
//                .orElseThrow(() -> new EntityNotFoundException("Payment not found: " + paymentId));
//
//        // Idempotency
//        if (payment.getStatus() == PaymentStatus.SUCCESS) return;
//        if (payment.getStatus() != PaymentStatus.PENDING) return;
//
//        // 2) Đối soát số tiền
//        BigDecimal expected = payment.getTotal(); // BigDecimal trong DB
//        BigDecimal received = BigDecimal.valueOf(data.getAmount().longValue()); // amount là Integer/Long
//
//        if (expected.compareTo(received) != 0) {
//            // Dùng 'getPaymentId()' (viết thường chữ 'd')
//            updatePaymentStatus(payment.getPaymentId(), PaymentStatus.FAILED, data.getReference());
//            throw new IllegalArgumentException("Amount mismatch: expected " + expected + " but got " + received);
//        }
//
//        // 3) Cập nhật thành công
//        payment.setStatus(PaymentStatus.SUCCESS);
//        payment.setPaymentTime(LocalDateTime.now());
//        payment.setGatewayTxnId(data.getReference());
//        paymentRepository.save(payment);
//
//        Booking booking = payment.getBooking();
//        booking.setStatus(BookingStatus.CONFIRMED);
//        bookingRepository.save(booking);
//    }
//
//    private void updatePaymentStatus(Long paymentId, PaymentStatus status, String gatewayTxnId) {
//        // Dùng 'getPaymentId()' (viết thường chữ 'd')
//        paymentRepository.findById(paymentId).ifPresent(p -> {
//            if (p.getStatus() == PaymentStatus.PENDING) {
//                p.setStatus(status);
//                p.setGatewayTxnId(gatewayTxnId);
//                paymentRepository.save(p);
//            }
//        });
//    }
//}
//
