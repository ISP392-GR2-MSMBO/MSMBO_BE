package com.example.ticket_booking_system.dto.Data;

import com.example.ticket_booking_system.entity.Promotion;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PriceResult {
    private Float finalPrice;
    private Promotion appliedPromotion; // Khuyến mãi đã được dùng (có thể là null)
}
