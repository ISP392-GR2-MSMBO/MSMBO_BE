package com.example.ticket_booking_system.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ticket_booking_system.entity.Seat;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByTheaterId(Long theaterId);
}
