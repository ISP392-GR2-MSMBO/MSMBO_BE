package com.example.ticket_booking_system.controller;


import com.example.ticket_booking_system.dto.request.promotion.CreatePromotionRequest;
import com.example.ticket_booking_system.dto.request.promotion.UpdatePromotionStatusRequest;
import com.example.ticket_booking_system.entity.Promotion;
import com.example.ticket_booking_system.service.PromotionService;
import jakarta.security.auth.message.callback.SecretKeyCallback;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/promotions")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class PromotionController {
    private final PromotionService promotionService;

    @PostMapping
    public ResponseEntity<Promotion> creeatePromotion(@Valid @RequestBody CreatePromotionRequest request) {
        Promotion newPromo = promotionService.createPromotion(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(newPromo);
    }

    @PostMapping("/{id}/apply-to-seat-types")
    public ResponseEntity<Void> applyPromotionToSeatTypes(
            @PathVariable("id") Long promotionId,
            @RequestBody List<Long> seatTypeIds
    ) {
        promotionService.applyPromotionToSeatTypes(promotionId, seatTypeIds);
        return ResponseEntity.ok().build(); // Trả về 200 OK
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Promotion> updatePromotionStatus(
            @PathVariable("id") Long promotionId,
            @RequestBody UpdatePromotionStatusRequest request
            ) {
        Promotion updatedPromo = promotionService.updatePromotionStatus(
                promotionId,
                request.getIsActive()
        );
        return ResponseEntity.ok(updatedPromo);
    }
}
