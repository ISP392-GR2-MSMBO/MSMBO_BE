package com.example.ticket_booking_system.repository;

import com.example.ticket_booking_system.dto.reponse.statistic.TopMovie;
import com.example.ticket_booking_system.entity.BookingDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingDetailRepository extends JpaRepository<BookingDetail, Long> {
    List<BookingDetail> findAllByBooking_Showtime_ShowtimeID(Long showtimeId);
    // -----------------------------------------------------------------
    // HÀM MỚI (ĐÃ FIX): Thêm query để đếm vé, lấy top phim
    // -----------------------------------------------------------------
    /**
     * Truy vấn này đếm số lượng vé (BookingDetail)
     * được nhóm theo tên phim, trong một khoảng ngày
     * và chỉ tính các booking đã CONFIRMED.
     *
     *  QUAN TRỌNG: Giả định Entity `Showtime` có liên kết tên là `movie`
     * và Entity `Movie` có thuộc tính tên là `movieName`.
     */
    @Query("SELECT new com.example.ticket_booking_system.dto.reponse.statistic.TopMovie(s.movie.movieName, COUNT(bd.bookingDetailID)) " +
            "FROM BookingDetail bd " +
            "JOIN bd.booking b " +
            "JOIN b.showtime s " +
            "JOIN s.movie m " +
            "WHERE b.bookingDate BETWEEN :startDate AND :endDate " +
            "AND b.status = com.example.ticket_booking_system.Enum.BookingStatus.CONFIRMED " +
            "GROUP BY m.movieName " +
            "ORDER BY COUNT(bd.bookingDetailID) DESC")
    List<TopMovie> findTopSellingMovies(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
