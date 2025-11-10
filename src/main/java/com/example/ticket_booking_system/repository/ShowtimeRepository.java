package com.example.ticket_booking_system.repository;

import com.example.ticket_booking_system.entity.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {
    // Overlap nếu: newStart < s.endTime AND newEnd > s.startTime (cùng theater & date)
    /**
     * Kiểm tra xem có bất kỳ suất chiếu nào (s) trong cùng một rạp (:theaterId)
     * vào cùng một ngày (:date) bị trùng lặp thời gian với khoảng thời gian mới hay không.
     * Logic trùng lặp (Overlap) là khi:
     * (Giờ bắt đầu mới < Giờ kết thúc cũ) VÀ (Giờ kết thúc mới > Giờ bắt đầu cũ)
     */
    @Query("""
           select (count(s) > 0) from Showtime s
           where s.theater.theaterID = :theaterId
             and s.date = :date
             and (CAST(:startTime as time) < s.endTime and CAST(:endTime as time) > s.startTime)
           """)
    boolean existsOverlap(@Param("theaterId") Long theaterId,
                          @Param("date") LocalDate date,
                          @Param("startTime") LocalTime startTime,
                          @Param("endTime") LocalTime endTime);


    /**
     * Lấy tất cả các suất chiếu trong hệ thống.
     * Sắp xếp theo ngày (tăng dần) và sau đó là giờ bắt đầu (tăng dần).
     */
    @Query("""
    SELECT s FROM Showtime s
    ORDER BY s.date ASC, s.startTime ASC
""")
    List<Showtime> findAllShowtime();
//    List<Showtime> findByMovie_MovieIDAndDateGreaterThanEqualOrderByDateAscStartTimeAsc(
//            Long movieId, LocalDate today
//    );

    /**
     * Tìm các suất chiếu cho một phim cụ thể (:movieId)
     * CHỈ BAO GỒM:
     * 1. Các suất chưa bị xóa mềm (IsDeletedFalse)
     * 2. Các suất từ ngày hôm nay (:date) trở về sau (DateGreaterThanEqual)
     * Sắp xếp theo ngày và giờ bắt đầu.
     * (Đây là query dùng cho trang public của khách hàng)
     */
    List<Showtime> findByMovie_MovieIDAndIsDeletedFalseAndDateGreaterThanEqualOrderByDateAscStartTimeAsc(
            Long movieId, LocalDate date
    );

    /**
     * Tìm suất chiếu public theo MovieID VÀ Tên Rạp (ví dụ: "2D", "IMAX")
     * Chỉ lấy các suất từ hôm nay trở về sau.
     */
    List<Showtime> findByMovie_MovieIDAndTheater_TheaterNameAndIsDeletedFalseAndDateGreaterThanEqualOrderByDateAscStartTimeAsc(
            Long movieId, String theaterName, LocalDate date
    );
}
