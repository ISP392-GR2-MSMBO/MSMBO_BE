package com.example.ticket_booking_system.service;

import com.example.ticket_booking_system.entity.Combo;
import com.example.ticket_booking_system.repository.ComboRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

// FoodService.java
@Service
@RequiredArgsConstructor
public class ComboService {
    private final ComboRepository comboRepository;

    // Chỉ cần 1 hàm này
    public List<Combo> getActiveCombos() {
        return comboRepository.findByIsActiveTrue();
    }
}
