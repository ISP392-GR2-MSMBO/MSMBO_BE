package com.example.ticket_booking_system.controller;

import com.example.ticket_booking_system.entity.Combo;
import com.example.ticket_booking_system.service.ComboService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// FoodController.java
@RestController
@RequestMapping("/api/combos")
@CrossOrigin(origins = "http://localhost:3000")// API public cho khách
@RequiredArgsConstructor
public class ComboController {
    private final ComboService comboService;

    @GetMapping
    public ResponseEntity<List<Combo>> getAllActiveCombos() {
        return ResponseEntity.ok(comboService.getActiveCombos());
    }
}
