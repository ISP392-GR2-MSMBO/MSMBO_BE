package com.example.ticket_booking_system.repository;


import com.example.ticket_booking_system.entity.Theater;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ticket_booking_system.entity.Seat;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByTheaterId(Long theaterId);

    boolean existsByTheaterIdAndRowAndNumber(Long theaterId, String row, Integer number);
}
