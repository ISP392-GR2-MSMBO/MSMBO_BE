package com.example.ticket_booking_system.service;

import com.example.ticket_booking_system.Enum.BookingStatus;
import com.example.ticket_booking_system.Enum.PaymentStatus;
import com.example.ticket_booking_system.controller.PaymentController;
import com.example.ticket_booking_system.entity.Booking;
import com.example.ticket_booking_system.entity.Payment;
import com.example.ticket_booking_system.repository.BookingRepository;
import com.example.ticket_booking_system.repository.PaymentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Tạo link

// Webhook
import vn.payos.model.webhooks.Webhook;      // body PayOS POST sang
import vn.payos.model.webhooks.WebhookData;  // dữ liệu sau khi verify

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class PaymentService {



    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final PayOSService payOSService; // service bọc PayOS SDK

    @Transactional
    public void handleWebhook(Webhook webhookBody) throws Exception {
        // 1) Xác minh chữ ký + parse nội dung (SDK 2.x)
        WebhookData data = payOSService.verifyPaymentWebhookData(webhookBody);
        if (data == null) throw new IllegalArgumentException("Invalid webhook");

        // 2) Tìm payment theo orderCode
        long paymentId = data.getOrderCode();
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found: " + paymentId));

        // Idempotent: nếu đã SUCCESS thì bỏ qua
        if (payment.getStatus() == PaymentStatus.SUCCESS) return;
        if (payment.getStatus() != PaymentStatus.PENDING) return;

        // 3) Đối soát số tiền
        BigDecimal expected = payment.getTotal();                    // BigDecimal trong DB
        BigDecimal received = BigDecimal.valueOf(data.getAmount());  // SDK trả int VND
        if (expected.compareTo(received) != 0) {
            // ghi thất bại & lý do
            payment.setStatus(PaymentStatus.FAILED);
            payment.setGatewayTxnId(data.getReference());
            paymentRepository.save(payment);
            throw new IllegalArgumentException("Amount mismatch: expected " + expected + " but got " + received);
        }

        // 4) Cập nhật thành công
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setPaymentTime(LocalDateTime.now());
        payment.setGatewayTxnId(data.getReference());
        paymentRepository.save(payment);

        // 5) Xác nhận booking
        Booking booking = payment.getBooking();
        booking.setStatus(BookingStatus.CONFIRMED);
        bookingRepository.save(booking);
    }
}
