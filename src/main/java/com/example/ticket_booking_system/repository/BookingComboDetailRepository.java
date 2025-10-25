package com.example.ticket_booking_system.repository;

import com.example.ticket_booking_system.entity.BookingComboDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingComboDetailRepository extends JpaRepository<BookingComboDetail, Long> {
    // không hàm custom ở đây
}
