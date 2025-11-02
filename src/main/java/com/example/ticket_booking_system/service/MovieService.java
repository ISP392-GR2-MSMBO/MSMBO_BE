package com.example.ticket_booking_system.service;


import com.example.ticket_booking_system.Enum.Approve;
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

//    Chức năng: Lấy danh sách tất cả các phim.
//    Logic: Chỉ trả về các phim chưa bị xóa mềm (isDeleted = false).
    public List<Movie> getAllMovies() {
        return movieRepository.findByIsDeletedFalse();
    }

//    Chức năng: Lấy thông tin chi tiết của một phim theo ID.
//    Logic: Nếu không tìm thấy phim, ném lỗi MOVIE_NOT_FOUND.
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
    // Thêm lại: tìm theo status (string) – có chuẩn hoá trước khi query
    public List<Movie> findByStatus(String rawStatus) {
        String normalized = normalizeStatus(rawStatus);
        return movieRepository.findByStatusIgnoreCase(normalized);
    }
    private String normalizeStatus(String rawStatus) {
        if (rawStatus == null || rawStatus.isBlank()) {
            return "Coming Soon"; // mặc định
        }
        String s = rawStatus.trim().toLowerCase();
        // các biến thể của Coming Soon
        if (s.contains("coming") || s.contains("soon")) {
            return "Coming Soon";
        }
        // các biến thể của Now Showing
        if (s.contains("now") || s.contains("showing")) {
            return "Now Showing";
        }
        // các biến thể của Ended
        if (s.contains("end")) {
            return "Ended";
        }
        throw new AppException(ErrorCode.INVALID_STATUS);
    }
    // Thêm movie mới
    public Movie saveMovie(@NonNull Movie movie) {
        if (movieRepository.existsByMovieNameIgnoreCase(movie.getMovieName())) {
            throw new AppException(ErrorCode.MOVIE_EXISTED);
        }
        movie.setStatus(normalizeStatus(movie.getStatus()));
        if (movie.getStatus() == null || movie.getStatus().isBlank()) {
            movie.setStatus("Coming Soon");
            movie.setApproveStatus(Approve.PENDING);
            movie.setPublished(false);
            movie.setDeleted(false);
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
        // Sử dụng Builder pattern với toBuilder()
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
                .poster(movieDetails.getPoster())
                .trailer(movieDetails.getTrailer())
                .status(normalizeStatus(movieDetails.getStatus()))
                .approveStatus(Approve.PENDING)
                .isPublished(false)
                .build();
        return movieRepository.save(updatedMovie);
    }
    public Movie updateBanner(Long id, String banner) {
        Movie movie = movieRepository.findByMovieIDAndIsDeletedFalse(id)
                .orElseThrow(() -> new AppException(ErrorCode.MOVIE_NOT_FOUND));
        movie.setBanner(banner);
        return movieRepository.save(movie);
    }
    // Xóa movie
    public void deleteMovie(Long id){
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.MOVIE_NOT_FOUND));
        if (movie.isDeleted()) {
            throw new AppException(ErrorCode.MOVIE_ALREADY_DELETED); // Tạo thêm error code nếu muốn
        }
        movie.setDeleted(true);
        movie.setPublished(false);// Đánh dấu là đã xóa
        movieRepository.save(movie); // Lưu lại
    }
    // ===== Admin actions =====
    public Movie approveMovie(Long id) {
        Movie m = movieRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.MOVIE_NOT_FOUND));
        if (m.isDeleted()) throw new AppException(ErrorCode.MOVIE_DELETED_OR_INACTIVE);
        m.setApproveStatus(Approve.APPROVE);
        m.setPublished(true);
        return movieRepository.save(m);
    }

    public Movie rejectMovie(Long id) {
        Movie m = movieRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.MOVIE_NOT_FOUND));
        if (m.isDeleted()) throw new AppException(ErrorCode.MOVIE_DELETED_OR_INACTIVE);
        m.setApproveStatus(Approve.DENIED);
        m.setPublished(false);
        return movieRepository.save(m);
    }
}
