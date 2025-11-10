package com.example.ticket_booking_system.service;

import com.example.ticket_booking_system.Enum.BookingStatus;
import com.example.ticket_booking_system.Enum.PaymentStatus;
import com.example.ticket_booking_system.entity.Booking;
import com.example.ticket_booking_system.entity.Payment;
import com.example.ticket_booking_system.repository.BookingRepository;
import com.example.ticket_booking_system.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookingCleanupService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository; // Thêm
    private final BookingService bookingService;

    // Hết hạn cho payment (người dùng đã qua trang thanh toán)
    private final long PAYMENT_EXPIRATION_MINUTES = 10;
    // Hết hạn cho booking mồ côi (người dùng tắt tab ở trang ghế)
    private final long ORPHAN_BOOKING_EXPIRATION_MINUTES = 3;

    // Chạy mỗi 2 phút
    @Scheduled(fixedRate = 120000)
    @Transactional
    public void cleanupExpiredBookings() {

        // --- TÁC VỤ 1: Dọn dẹp Payment (PENDING) đã hết hạn ---
        // (Áp dụng khi người dùng qua trang payment nhưng không thanh toán)
        try {
            LocalDateTime paymentExpirationTime = LocalDateTime.now().minusMinutes(PAYMENT_EXPIRATION_MINUTES);
            List<Payment> expiredPayments = paymentRepository.findAllByStatusAndPaymentTimeBefore(
                    PaymentStatus.PENDING,
                    paymentExpirationTime
            );

            if (!expiredPayments.isEmpty()) {
                log.info("Found {} expired PENDING payments. Cleaning up...", expiredPayments.size());
                for (Payment payment : expiredPayments) {
                    Long bookingId = payment.getBooking().getBookingID();
                    bookingService.hardDeleteBooking(bookingId);
                    log.warn("Expired booking (ID: {}) associated with payment (ID: {}) has been deleted.",
                            bookingId, payment.getPaymentId());
                }
            }
        } catch (Exception e) {
            log.error("Error during expired payment cleanup: {}", e.getMessage(), e);
        }

        // --- TÁC VỤ 2: Dọn dẹp Booking (mồ côi) đã hết hạn ---
        // (Áp dụng khi người dùng tắt tab ở trang chọn ghế)
        try {
            LocalDateTime orphanExpirationTime = LocalDateTime.now().minusMinutes(ORPHAN_BOOKING_EXPIRATION_MINUTES);
            List<Booking> orphanBookings = bookingRepository.findOrphanPendingBookings(
                    BookingStatus.PENDING,
                    orphanExpirationTime
            );

            if (!orphanBookings.isEmpty()) {
                log.info("Found {} orphan PENDING bookings. Cleaning up...", orphanBookings.size());
                for (Booking booking : orphanBookings) {
                    // Xóa trực tiếp vì nó chưa có payment (không cần gọi hardDeleteBooking)
                    bookingRepository.delete(booking);
                    log.warn("Orphan booking (ID: {}) created at {} has been deleted.",
                            booking.getBookingID(), booking.getCreatedAt());
                }
            }
        } catch (Exception e) {
            log.error("Error during orphan booking cleanup: {}", e.getMessage(), e);
        }
    }
}