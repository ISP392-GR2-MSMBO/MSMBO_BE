package com.example.ticket_booking_system.repository;


import com.example.ticket_booking_system.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.ticket_booking_system.Enum.BookingStatus;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserUserIDOrderByBookingDateDesc(Long userId);

    // -----------------------------------------------------------------
    // ✅ HÀM MỚI (ĐÃ FIX): Thêm query để lấy doanh thu theo tháng
    // -----------------------------------------------------------------
    /**
     * Truy vấn này trả về một List các object,
     * mỗi object chứa [Integer month, Float totalRevenue]
     * cho một năm cụ thể và CHỈ TÍNH CÁC VÉ ĐÃ CONFIRMED.
     */
    @Query("SELECT MONTH(b.bookingDate) as month, SUM(b.totalPrice) as revenue " +
            "FROM Booking b " +
            "WHERE YEAR(b.bookingDate) = :year " +
            "AND b.status = BookingStatus.CONFIRMED " +
            "GROUP BY MONTH(b.bookingDate) " +
            "ORDER BY month ASC")
    List<Object[]> getMonthlyRevenueByYear(@Param("year") int year);
}
