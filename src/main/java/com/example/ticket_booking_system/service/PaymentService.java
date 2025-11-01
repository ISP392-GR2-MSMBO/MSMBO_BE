package com.example.ticket_booking_system.service;

import com.example.ticket_booking_system.Enum.BookingStatus;
import com.example.ticket_booking_system.Enum.PaymentStatus;
import com.example.ticket_booking_system.dto.request.payment.CreatePaymentRequest;
import com.example.ticket_booking_system.entity.Booking;
import com.example.ticket_booking_system.entity.Payment;
import com.example.ticket_booking_system.repository.BookingRepository;
import com.example.ticket_booking_system.repository.PaymentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

// Webhook
import vn.payos.PayOS;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkRequest;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkResponse;
import vn.payos.model.v2.paymentRequests.PaymentLinkItem; // body PayOS POST sang
import vn.payos.model.webhooks.WebhookData;  // dữ liệu sau khi verify

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final PayOS payOS;

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    // BỔ SUNG 2: Lấy URL từ file properties
    @Value("${payos.return-url}")
    private String returnUrl;

    @Value("${payos.cancel-url}")
    private String cancelUrl;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public CreatePaymentLinkResponse createPaymentLink(CreatePaymentRequest request) throws Exception {

        // 1. Tìm booking
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new EntityNotFoundException("Booking not found: " + request.getBookingId()));

        // 2. Kiểm tra trạng thái booking
        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new IllegalStateException("Booking không ở trạng thái PENDING, không thể thanh toán.");
        }

        // 3. Tạo đối tượng Payment mới
        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setTotal(BigDecimal.valueOf(booking.getTotalPrice()));
        payment.setStatus(PaymentStatus.PENDING);

        payment.setPaymentMethod("PAYOS");
        payment.setPaymentTime(LocalDateTime.now()); // Set thời gian tạo

        // 4. LƯU payment xuống DB
        Payment savedPayment = paymentRepository.save(payment);
        paymentRepository.flush();

        // 5. Dùng ID của payment vừa lưu làm orderCode
        // Đây là bước QUAN TRỌNG để webhook của bạn hoạt động
        long orderCode = savedPayment.getPaymentId();

        String description = "Thanh toan ve " + booking.getBookingID();
        long amount = savedPayment.getTotal().longValue(); // Dùng long cho builder

        long bookingId = booking.getBookingID();

        String dynamicReturnUrl = returnUrl + "?bookingId=" + bookingId;

        String dynamicCancelUrl = cancelUrl + "?bookingId=" + bookingId;

        // 6. TẠO REQUEST GIỐNG CODE DEMO

        // Tạo một "item" (sản phẩm)
        PaymentLinkItem item = PaymentLinkItem.builder()
                .name(description)
                .quantity(1)
                .price(amount)
                .build();

        // Tạo đối tượng request chính
        CreatePaymentLinkRequest paymentData = CreatePaymentLinkRequest.builder()
                .orderCode(orderCode)         // Dùng orderCode từ DB của mình
                .description(description)
                .amount(amount)
                .item(item)                   // Thêm item vào
                .returnUrl(dynamicReturnUrl)         // Lấy từ properties
                .cancelUrl(dynamicCancelUrl)         // Lấy từ properties
                .build();

        // 7. GỌI SDK THEO CÁCH CỦA DEMO
        CreatePaymentLinkResponse data = payOS.paymentRequests().create(paymentData);

        // 8. Trả về đối tượng response từ PayOS
        return data;
    }

    @Transactional
    public boolean handleWebhook(WebhookData data) throws Exception {

        long paymentId = data.getOrderCode();
        if (data == null) throw new IllegalArgumentException("Invalid webhook");

        // 1. Tìm payment
        Optional<Payment> paymentOpt = paymentRepository.findById(paymentId);

        // 2. NẾU KHÔNG TÌM THẤY (Lỗi 123 của bạn)
        if (paymentOpt.isEmpty()) {
            logger.warn("Webhook for non-existent paymentId {} received. Ignoring.", paymentId);
            return false; // <-- TRẢ VỀ FALSE, KHÔNG VĂNG LỖI
        }

        Payment payment = paymentOpt.get();

        // Idempotent: nếu đã SUCCESS thì bỏ qua
        if (payment.getStatus() == PaymentStatus.SUCCESS) return true;
        if (payment.getStatus() != PaymentStatus.PENDING) return true;

        // 3) Đối soát số tiền
        BigDecimal expected = payment.getTotal();                    // BigDecimal trong DB
        BigDecimal received = BigDecimal.valueOf(data.getAmount());  // SDK trả int VND
        if (expected.compareTo(received) != 0) {
            // ghi thất bại & lý do
            logger.warn("Amount mismatch for paymentId {}: expected {} but got {}",
                    paymentId, expected, received);

            payment.setStatus(PaymentStatus.FAILED);
            payment.setGatewayTxnId(data.getReference());
            paymentRepository.save(payment);
            return true;
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
        return true;
    }


}
