package com.example.ticket_booking_system.repository;

import com.example.ticket_booking_system.entity.Theater;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TheaterRepository extends JpaRepository<Theater, Long> {
}
