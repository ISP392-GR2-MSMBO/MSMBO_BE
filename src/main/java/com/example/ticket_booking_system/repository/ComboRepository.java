package com.example.ticket_booking_system.repository;

import com.example.ticket_booking_system.entity.Combo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComboRepository extends JpaRepository<Combo, Long> {

    // Tìm các combo đang hoạt động để hiển thị cho người dùng
    List<Combo> findByIsActiveTrue();
}
