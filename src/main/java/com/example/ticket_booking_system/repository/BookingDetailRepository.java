package com.example.ticket_booking_system.repository;

import com.example.ticket_booking_system.entity.BookingDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingDetailRepository extends JpaRepository<BookingDetail, Long> {
    // không hàm custom ở đây
}
