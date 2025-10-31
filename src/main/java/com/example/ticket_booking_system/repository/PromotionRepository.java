package com.example.ticket_booking_system.repository;

import com.example.ticket_booking_system.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {

    /**
     * Hàm này sẽ tìm tất cả các khuyến mãi:
     * 1. Đang được kích hoạt (isActive = true)
     * 2. Còn hạn (ngày kiểm tra nằm giữa ngày bắt đầu và kết thúc)
     * 3. Có áp dụng cho một loại ghế (seatTypeId) cụ thể
     */
    @Query("SELECT p FROM Promotion p " +
            "JOIN p.applicableSeatTypes st " +
            "WHERE st.seatTypeID = :seatTypeId " +
            "AND p.isActive = true " +
            "AND :dateToCheck BETWEEN p.startDate AND p.endDate")
    List<Promotion> findActivePromotionsForSeatType(
            @Param("seatTypeId") Long seatTypeId,
            @Param("dateToCheck") LocalDate dateToCheck
    );

    boolean existsByName(String name);

    /**
     * HÀM MỚI: Kiểm tra xem có KM nào (đang active)
     * bị trùng lặp ngày với khoảng thời gian mới hay không
     */
    @Query("SELECT COUNT(p) > 0 FROM Promotion p " +
            "WHERE p.isActive = true " + // Chỉ kiểm tra các KM đang hoạt động
            "AND p.startDate <= :newEndDate " +
            "AND p.endDate >= :newStartDate")
    boolean existsOverlappingPromotion(
            @Param("newStartDate") LocalDate newStartDate,
            @Param("newEndDate") LocalDate newEndDate
    );

    /**
     * HÀM MỚI (3 THAM SỐ): Dùng cho updatePromotionStatus
     * (Bạn cũng nên thêm hàm này với tên 'WithOthers' để tránh nhầm lẫn)
     */
    @Query("SELECT COUNT(p) > 0 FROM Promotion p " +
            "WHERE p.isActive = true " +
            "AND p.promotionID != :excludeId " + // <-- Loại trừ chính nó
            "AND p.startDate <= :newEndDate " +
            "AND p.endDate >= :newStartDate")
    boolean existsOverlappingPromotionWithOthers(
            @Param("newStartDate") LocalDate newStartDate,
            @Param("newEndDate") LocalDate newEndDate,
            @Param("excludeId") Long excludeId
    );
}
