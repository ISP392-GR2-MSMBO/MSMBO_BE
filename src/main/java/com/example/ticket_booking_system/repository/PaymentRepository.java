package com.example.ticket_booking_system.repository;

import com.example.ticket_booking_system.Enum.PaymentStatus;
import com.example.ticket_booking_system.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByBooking_BookingIDAndStatus(Long bookingID, PaymentStatus status);
    void deleteByBooking_BookingID(Long bookingID);
}
