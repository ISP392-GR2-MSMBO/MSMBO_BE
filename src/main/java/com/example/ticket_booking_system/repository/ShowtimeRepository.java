package com.example.ticket_booking_system.repository;

import com.example.ticket_booking_system.entity.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {
    List<Showtime> findByMovie_MovieID(Long movieID);
    // Tuỳ chọn: lọc thêm theo rạp
    List<Showtime> findByMovie_MovieNameContainingIgnoreCaseAndTheater_TheaterID(
            String keyword, Long theaterId);
    // Tuỳ chọn: kèm cả ngày & rạp
    List<Showtime> findByMovie_MovieNameContainingIgnoreCaseAndTheater_TheaterIDAndDate(
            String keyword, Long theaterId, java.time.LocalDate date);
    // Tìm showtime theo *một phần* tên phim (không phân biệt hoa/thường)
    List<Showtime> findByMovie_MovieNameContainingIgnoreCase(String keyword);
    // Tuỳ chọn: lọc thêm theo ngày
    List<Showtime> findByMovie_MovieNameContainingIgnoreCaseAndDate(
            String keyword, java.time.LocalDate date);
    // Overlap nếu: newStart < s.endTime AND newEnd > s.startTime (cùng theater & date)
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
}
