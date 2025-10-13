package com.example.ticket_booking_system.controller;

import com.example.ticket_booking_system.entity.Showtime;
import com.example.ticket_booking_system.exception.AppException;
import com.example.ticket_booking_system.exception.ErrorCode;
import com.example.ticket_booking_system.service.ShowtimeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.example.ticket_booking_system.entity.Movie;
import com.example.ticket_booking_system.service.MovieService;

import java.util.List;

import com.example.ticket_booking_system.dto.request.movie.MovieRequest;
import com.example.ticket_booking_system.dto.reponse.movie.MovieResponse;
//import com.example.ticket_booking_system.mapper.MovieMapper;

@RestController
@RequiredArgsConstructor
@RequestMapping("/movies")
@CrossOrigin(origins = "http://localhost:3000") // cần thêm khi nối React
public class MovieController {
    private final MovieService movieService;
    private final ShowtimeService showtimeService;
    @Autowired
    private final ModelMapper modelMapper;
    @GetMapping
    public List<MovieResponse> getAllMovies() {
        return movieService.getAllMovies()
                .stream()
                .map(m -> modelMapper.map(m, MovieResponse.class))
                .toList();
    }

    @GetMapping("/{movieID}")
    public MovieResponse getMovie(@PathVariable("movieID") Long movieId) {
        Movie m = movieService.getMovie(movieId);
        return modelMapper.map(m, MovieResponse.class);
    }

    // 1) Chỉ khi CÓ status và KHÔNG CÓ name
    @GetMapping(value = "/searchStatus", params = "status")
    public ResponseEntity<?> searchByStatus(@RequestParam String status) {
        var movies = movieService.findByStatus(status);
        if (movies.isEmpty()) throw new AppException(ErrorCode.MOVIE_NOT_FOUND);
        var res = movies.stream().map(m -> modelMapper.map(m, MovieResponse.class)).toList();
        return ResponseEntity.ok(res);
    }


    @GetMapping(value = "/search", params = "name")
    public ResponseEntity<?> searchMovies(@RequestParam String name) {
        List<Movie> movies = movieService.searchMoviesByKeyword(name);
        if (movies.isEmpty()) throw new AppException(ErrorCode.MOVIE_NOT_FOUND);
        List<MovieResponse> res = movies.stream()
                .map(m -> modelMapper.map(m, MovieResponse.class))
                .toList();
        return ResponseEntity.ok(res);
    }

    @PostMapping
    public MovieResponse createMovie(@Valid @RequestBody MovieRequest request) {
        Movie toSave = modelMapper.map(request, Movie.class);
        Movie saved = movieService.saveMovie(toSave);
        return modelMapper.map(saved, MovieResponse.class); // map từ entity đã lưu
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovieResponse> updateMovie(@PathVariable Long id,
                                                     @Valid @RequestBody MovieRequest request) {
        Movie toUpdate = modelMapper.map(request, Movie.class);
        Movie updated = movieService.updateMovie(id, toUpdate);
        return ResponseEntity.ok(modelMapper.map(updated, MovieResponse.class));
    }

    // Xoá phim
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }
}