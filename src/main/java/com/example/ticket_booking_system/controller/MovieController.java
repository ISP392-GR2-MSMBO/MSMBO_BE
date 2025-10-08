package com.example.ticket_booking_system.controller;

import com.example.ticket_booking_system.exception.AppException;
import com.example.ticket_booking_system.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.ticket_booking_system.entity.Movie;
import com.example.ticket_booking_system.service.MovieService;

import java.util.List;

import com.example.ticket_booking_system.dto.request.movie.MovieRequest;
import com.example.ticket_booking_system.dto.reponse.movie.MovieResponse;
import com.example.ticket_booking_system.mapper.MovieMapper;

@RestController
@RequestMapping("/movies")
//@CrossOrigin(origins = "http://localhost:3000") // cần thêm khi nối React
public class MovieController {
    private final MovieService movieService;
    @Autowired
    public MovieController(MovieService movieService){
        this.movieService=movieService;
    }
    @GetMapping
    public List<MovieResponse> getAllMovies(){
        List<MovieResponse> result = new java.util.ArrayList<>();
        for (Movie m : movieService.getAllMovies()) {
            result.add(MovieMapper.toResponse(m));
        }
        return result;
    }
    @GetMapping("/{movieID}")
    public MovieResponse getMovie(@PathVariable("movieID") Long movieId) {
        return MovieMapper.toResponse(movieService.getMovie(movieId));
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchMovies(@RequestParam String name) {
        List<Movie> movies = movieService.searchMoviesByKeyword(name);
        if (movies.isEmpty()) {
            throw new AppException(ErrorCode.MOVIE_NOT_FOUND);
        }
        List<MovieResponse> res = new java.util.ArrayList<>();
        for (Movie m : movies) {
            res.add(MovieMapper.toResponse(m));
        }
        return ResponseEntity.ok(res);
    }

    // Thêm phim
    @PostMapping
    public MovieResponse createMovie(@RequestBody MovieRequest request) {
        Movie toSave = MovieMapper.toEntity(request);
        return MovieMapper.toResponse(movieService.saveMovie(toSave));
    }

    // Cập nhật phim
    @PutMapping("/{id}")
    public ResponseEntity<MovieResponse> updateMovie(@PathVariable Long id, @RequestBody MovieRequest request) {
        Movie updated = movieService.updateMovie(id, MovieMapper.toEntity(request));
        return ResponseEntity.ok(MovieMapper.toResponse(updated));
    }

    // Xoá phim
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }
    }
