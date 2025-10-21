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
    @Query("""
      SELECT s FROM Showtime s
      WHERE s.isPublished = true
        AND s.approveStatus = Approve.APPROVE
        AND s.movie.isDeleted = false
        AND s.date >= :today
      ORDER BY s.date, s.startTime
    """)
    List<Showtime> findPublicShowtime(LocalDate today);
//    List<Showtime> findByMovie_MovieIDAndDateGreaterThanEqualOrderByDateAscStartTimeAsc(
//            Long movieId, LocalDate today
//    );
    List<Showtime> findByMovie_MovieIDAndIsDeletedFalseAndDateGreaterThanEqualOrderByDateAscStartTimeAsc(
            Long movieId, LocalDate date
    );
}
