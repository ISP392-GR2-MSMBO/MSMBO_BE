package com.example.ticket_booking_system.service;

import com.example.ticket_booking_system.dto.request.promotion.CreatePromotionRequest;
import com.example.ticket_booking_system.entity.Promotion;
import com.example.ticket_booking_system.entity.SeatType;
import com.example.ticket_booking_system.exception.AppException;
import com.example.ticket_booking_system.exception.ErrorCode;
import com.example.ticket_booking_system.repository.PromotionRepository;
import com.example.ticket_booking_system.repository.SeatTypeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
@AllArgsConstructor
public class PromotionService {
    private final PromotionRepository promotionRepository;
    private final SeatTypeRepository seatTypeRepository;

    @Transactional
    public Promotion createPromotion(CreatePromotionRequest request){
        if(promotionRepository.existsByName(request.getName())){
            throw new AppException(ErrorCode.PROMOTION_NAME_EXISTS);
        }

        if(request.getEndDate().isBefore(request.getStartDate())) {
            throw new AppException(ErrorCode.INVALID_DATE_RANGE);
        }

        Promotion promotion = new Promotion();
        promotion.setName(request.getName());
        promotion.setStartDate(request.getStartDate());
        promotion.setEndDate(request.getEndDate());
        promotion.setDiscountType(request.getDiscountType());
        promotion.setDiscountValue(request.getDiscountValue());
        promotion.setActive(true);

        promotion.setApplicableSeatTypes(new HashSet<>());

        if (request.getSeatTypeIds() != null && !request.getSeatTypeIds().isEmpty()) {
            // 1. Tìm tất cả SeatType Entities từ list ID
            List<SeatType> seatTypes = seatTypeRepository.findAllById(request.getSeatTypeIds());

            // 2. Thêm chúng vào Set của promotion
            promotion.getApplicableSeatTypes().addAll(seatTypes);
        }

        return promotionRepository.save(promotion);
    }

    @Transactional
    public void applyPromotionToSeatTypes(Long promotionId, List<Long> seatTypeIds) {

        // 1. Tìm khuyến mãi, nếu không thấy thì báo lỗi
        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new AppException(ErrorCode.PROMOTION_NOT_FOUND));

        // 2. Tìm tất cả các loại ghế theo danh sách ID
        List<SeatType> seatTypes = seatTypeRepository.findAllById(seatTypeIds);

        // 3. Lấy danh sách (Set) các loại ghế hiện tại của KM và thêm list mới vào
        // Sẽ không lỗi Null vì chúng ta đã khởi tạo Set ở hàm createPromotion
        promotion.getApplicableSeatTypes().addAll(seatTypes);

        // 4. Lưu lại
        promotionRepository.save(promotion);
    }

    //lay danh sách tất cả khuyến mãi
    public List<Promotion> getAllPromotions(){

        return promotionRepository.findAll();
    }

    //lấy chi tiết 1 khuyến mãi bằng ID
    public Promotion getPromotionById(Long promotionId){
        return promotionRepository.findById(promotionId).orElseThrow(() -> new AppException(ErrorCode.PROMOTION_NOT_FOUND));
    }

    @Transactional
    public Promotion updatePromotionStatus(Long promotionId, boolean isActive) {
        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new AppException(ErrorCode.PROMOTION_NOT_FOUND));

        promotion.setActive(isActive);
        return promotionRepository.save(promotion);
    }
}
