package com.example.ticket_booking_system.dto.request.promotion;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePromotionStatusRequest {

    @NotNull(message = "Status isActive must not empty")
    private Boolean isActive; // Dùng Boolean (object) thay vì boolean (primitive)
}