package com.example.ticket_booking_system.controller;

import com.example.ticket_booking_system.exception.AppException;
import com.example.ticket_booking_system.exception.ErrorCode;
import com.example.ticket_booking_system.mapper.MovieMapper;
import com.example.ticket_booking_system.service.ShowtimeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.ticket_booking_system.entity.Movie;
import com.example.ticket_booking_system.service.MovieService;

import java.util.List;

import com.example.ticket_booking_system.dto.request.movie.MovieRequest;
import com.example.ticket_booking_system.dto.reponse.movie.MovieResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/movie")
@CrossOrigin(origins = "http://localhost:3000") // cần thêm khi nối React
public class MovieController {
    private final MovieService movieService;
    private final ShowtimeService showtimeService;
    @Autowired
    @GetMapping
    public List<MovieResponse> getAllMovies() {
        return movieService.getAllMovies()
                .stream()
                .map(MovieMapper::toResponse)
                .toList();
    }

    // 1) Chỉ khi CÓ status và KHÔNG CÓ name
    @GetMapping("/status/{status}")
    public ResponseEntity<?> searchByStatus(@PathVariable String status) {
        var movies = movieService.findByStatus(status);
        if (movies.isEmpty()) throw new AppException(ErrorCode.MOVIE_NOT_FOUND);
        var res = movies.stream().map(MovieMapper::toResponse).toList();
        return ResponseEntity.ok(res);
    }


    @GetMapping("/{name}")
    public ResponseEntity<?> searchMovies(@PathVariable  String name) {
        List<Movie> movies = movieService.searchMoviesByKeyword(name);
        if (movies.isEmpty()) throw new AppException(ErrorCode.MOVIE_NOT_FOUND);
        List<MovieResponse> res = movies.stream()
                .map(MovieMapper::toResponse)
                .toList();
        return ResponseEntity.ok(res);
    }

    @PostMapping
    public MovieResponse createMovie(@Valid @RequestBody MovieRequest request) {
        Movie movie = MovieMapper.toEntity(request);
        Movie saved = movieService.saveMovie(movie);
        return MovieMapper.toResponse(saved); // map từ entity đã lưu
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovieResponse> updateMovie(@PathVariable Long id,
                                                     @Valid @RequestBody MovieRequest request) {
        Movie movie = MovieMapper.toEntity(request);
        Movie updated = movieService.updateMovie(id, movie);
        return ResponseEntity.ok(MovieMapper.toResponse(updated));
    }
    // Xoá phim
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }
    // ==== Admin endpoints (tối giản) ====
    @PostMapping("/{id}/approve")
    public MovieResponse approve(@PathVariable Long id) {
        return MovieMapper.toResponse(movieService.approveMovie(id));
    }

    @PostMapping("/{id}/reject")
    public MovieResponse reject(@PathVariable Long id) {
        return MovieMapper.toResponse(movieService.rejectMovie(id));
    }
}