package com.example.ticket_booking_system.service;


import com.example.ticket_booking_system.entity.Movie;
import com.example.ticket_booking_system.exception.AppException;
import com.example.ticket_booking_system.exception.ErrorCode;
import com.example.ticket_booking_system.repository.MovieRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor

public class MovieService {
    private final MovieRepository movieRepository;

    /*public Optional<Movie> getMovieById(Long id){
        return movieRepository.findById(id);
    }*/
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Movie getMovie(Long movieId) {
        return movieRepository.findById(movieId)
                .orElseThrow(() -> new AppException(ErrorCode.MOVIE_NOT_FOUND));
    }

    public Movie getMovieByName(String movieName) {
        return movieRepository.findByMovieNameIgnoreCase(movieName)
                .orElseThrow(() -> new AppException(ErrorCode.MOVIE_NOT_FOUND));
    }
//    public List<Movie> findByStatus(String status) {
//        return movieRepository.findByStatusIgnoreCase(status);
//    }

    // Tìm kiếm gần đúng
    public List<Movie> searchMoviesByKeyword(String keyword) {
        return movieRepository.findByMovieNameContainingIgnoreCase(keyword);
    }

    // Thêm movie mới
    public Movie saveMovie(@NonNull Movie movie) {
        if (movieRepository.existsByMovieNameIgnoreCase(movie.getMovieName())) {
            throw new AppException(ErrorCode.MOVIE_EXISTED);
        }
        if (movie.getStatus() == null || movie.getStatus().isBlank()) {
            movie.setStatus("Coming Soon");
        } else {
            switch (movie.getStatus().trim().toLowerCase()) {
                case "now showing" -> movie.setStatus("Now Showing");
                case "coming soon" -> movie.setStatus("Coming Soon");
                case "ended" -> movie.setStatus("Ended");
                default -> throw new AppException(ErrorCode.INVALID_STATUS);
            }
        }
        return movieRepository.save(movie);
    }

    // Cập nhật movie
    public Movie updateMovie(Long id, @NonNull Movie movieDetails) {
        if (id == null) {
            throw new AppException(ErrorCode.MISSING_MOVIE_ID);
        }
        Movie existingMovie = movieRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.MOVIE_NOT_FOUND));

        // ✅ Sử dụng Builder pattern với toBuilder()
        Movie updatedMovie = existingMovie.toBuilder()
                .movieName(movieDetails.getMovieName())
                .genre(movieDetails.getGenre())
                .duration(movieDetails.getDuration())
                .age(movieDetails.getAge())
                .director(movieDetails.getDirector())
                .actress(movieDetails.getActress())
                .releaseDate(movieDetails.getReleaseDate())
                .language(movieDetails.getLanguage())
                .description(movieDetails.getDescription())
                .status(movieDetails.getStatus())
                .build();

        return movieRepository.save(updatedMovie);
    }

    // Xóa movie
    public void deleteMovie(Long id){
        if (!movieRepository.existsById(id)) {
            throw new AppException(ErrorCode.MOVIE_NOT_FOUND);
        }
        movieRepository.deleteById(id);
    }
}
