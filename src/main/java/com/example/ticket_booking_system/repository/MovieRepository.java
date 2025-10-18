package com.example.ticket_booking_system.repository;

import com.example.ticket_booking_system.Enum.Approve;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ticket_booking_system.entity.Movie;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    boolean existsByMovieNameIgnoreCase(String movieName);
    Optional<Movie> findByMovieNameIgnoreCase(String movieName);
    // Thêm phương thức tìm kiếm gần đúng (LIKE)
    List<Movie> findByMovieNameContainingIgnoreCase(String keyword);
    List<Movie> findByStatusIgnoreCase(@Param("status")String status);
    List<Movie> findByIsDeletedFalse();
    Optional<Movie> findByMovieIDAndIsDeletedFalse(Long movieID);
    // Thêm dòng này để kiểm tra tồn tại movie chưa bị xóa
    boolean existsByMovieIDAndIsDeletedFalse(Long movieID);
    // dùng cho public (đã publish & chưa xóa)
    //List<Movie> findByIsDeletedFalseAndIsPublishedTrue();
    Optional<Movie> findByMovieIDAndIsDeletedFalseAndIsPublishedTrue(Long movieID);
    Optional<Movie> findByMovieIDAndIsDeletedFalseAndApproveStatus(Long id, Approve status);
}
