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
}
