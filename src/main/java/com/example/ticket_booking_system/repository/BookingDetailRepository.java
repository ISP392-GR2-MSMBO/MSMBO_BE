package com.example.ticket_booking_system.repository;

import com.example.ticket_booking_system.entity.BookingDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingDetailRepository extends JpaRepository<BookingDetail, Long> {
    List<BookingDetail> findAllByBooking_Showtime_ShowtimeID(Long showtimeId);
}
