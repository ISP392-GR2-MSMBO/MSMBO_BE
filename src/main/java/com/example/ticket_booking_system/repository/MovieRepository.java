package com.example.ticket_booking_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ticket_booking_system.entity.Movie;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    boolean existsByMovieNameIgnoreCase(String movieName);
    Optional<Movie> findByMovieNameIgnoreCase(String movieName);
    // Thêm phương thức tìm kiếm gần đúng (LIKE)
    List<Movie> findByMovieNameContainingIgnoreCase(String keyword);
}
