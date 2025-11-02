package com.example.ticket_booking_system.controller;


import com.example.ticket_booking_system.dto.request.promotion.ApplyPromotionRequest;
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
//@CrossOrigin(origins = "http://localhost:3000")
@CrossOrigin(origins = "https://chillcinema.vercel.app/") // cần thêm khi nối React
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
            @Valid @RequestBody ApplyPromotionRequest request    ) {
        promotionService.applyPromotionToSeatTypes(promotionId, request.getSeatTypeID());
        return ResponseEntity.ok().build(); // Trả về 200 OK
    }

    @GetMapping
    public ResponseEntity<List<Promotion>> getAllPromotions() {
        List<Promotion> promotions = promotionService.getAllPromotions();
        return ResponseEntity.ok(promotions);
    }

    /**
     * API MỚI: Lấy MỘT khuyến mãi theo ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Promotion> getPromotionById(@PathVariable("id") Long promotionId) {
        Promotion promotion = promotionService.getPromotionById(promotionId);
        return ResponseEntity.ok(promotion);
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

    @DeleteMapping("/{id}/hard-delete")
    public ResponseEntity<Void> hardDeletePromotion(@PathVariable("id") Long promotionId) {
        promotionService.hardDeletePromotion(promotionId);
        // Trả về 204 No Content - nghĩa là "Xóa thành công, không có gì để trả về"
        return ResponseEntity.noContent().build();
    }

    /**
     * API MỚI: Chỉ lấy URL hình ảnh của một khuyến mãi
     */
    @GetMapping("/{id}/image")
    public ResponseEntity<Map<String, String>> getPromotionImage(@PathVariable("id") Long promotionId) {
        // 1. Dùng lại hàm service đã có để lấy entity
        Promotion promotion = promotionService.getPromotionById(promotionId);

        // 2. Tạo một đối tượng Map đơn giản chỉ chứa imageUrl
        // (Nếu bạn muốn, bạn có thể tạo DTO riêng cho việc này)
        Map<String, String> response = Map.of("imageUrl", promotion.getImageUrl());

        return ResponseEntity.ok(response);
    }
}
