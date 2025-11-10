package com.example.ticket_booking_system.service;


import com.example.ticket_booking_system.dto.Data.PriceResult;
import com.example.ticket_booking_system.entity.Promotion;
import com.example.ticket_booking_system.entity.SeatType;
import com.example.ticket_booking_system.repository.PromotionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class PriceService {
    private final PromotionRepository promotionRepository;

    public PriceResult calculateFinalPrice(SeatType seatType){
        Float basePrice = seatType.getBasePrice();
        LocalDate today = LocalDate.now();

        List<Promotion> promotionList = promotionRepository.findActivePromotionsForSeatType(seatType.getSeatTypeID(), today);

        if(promotionList.isEmpty()){
            return new PriceResult(basePrice, null);
        }

        //Ap dung KM
        Float bestPrice = basePrice;
        Promotion bestPromotion = null;

        for (Promotion p : promotionList){
            Float discountPrice = basePrice;
            if("percentage".equals(p.getDiscountType())){
                discountPrice = basePrice * (1- p.getDiscountValue() /100);
            } else if ("fixed_amount".equals(p.getDiscountType())) {
                discountPrice = basePrice - p.getDiscountValue();
            }

            Float roundedDiscountPrice = (float) Math.floor(discountPrice);

            if(roundedDiscountPrice < bestPrice){
                bestPrice = roundedDiscountPrice;
                bestPromotion = p;
            }
        }

        Float finalBestPrice = (bestPrice < 0) ? 0: bestPrice; // tranh gia am
        return new PriceResult(finalBestPrice, bestPromotion);
    }
}