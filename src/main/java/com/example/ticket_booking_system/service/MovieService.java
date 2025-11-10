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

    // Tìm kiếm gần đúng theo từ khóa (dựa trên tên phim)
    public List<Movie> searchMoviesByKeyword(String keyword) {
        return movieRepository.findByMovieNameContainingIgnoreCase(keyword);
    }


    // Thêm lại: tìm theo status (string) – có chuẩn hoá trước khi query
    /**
     * Chức năng: Tìm phim theo trạng thái (Now Showing, Coming Soon, Ended).
     * Logic: Chuẩn hóa chuỗi trạng thái đầu vào trước khi truy vấn.
     */
    public List<Movie> findByStatus(String rawStatus) {
        String normalized = normalizeStatus(rawStatus);
        return movieRepository.findByStatusIgnoreCase(normalized);
    }

    /**
     * Chức năng nội bộ: Chuẩn hóa chuỗi trạng thái.
     * Ví dụ: "coming" hoặc "soon" đều được chuẩn hóa thành "Coming Soon".
     */
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


    /**
     * Chức năng: Thêm một phim mới.
     */
    public Movie saveMovie(@NonNull Movie movie) {
        // 1. Kiểm tra nếu tên phim đã tồn tại (MOVIE_EXISTED).
        if (movieRepository.existsByMovieNameIgnoreCase(movie.getMovieName())) {
            throw new AppException(ErrorCode.MOVIE_EXISTED);
        }

        // 2. Chuẩn hóa trạng thái (normalizeStatus).
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

        // 3. Tự động đặt trạng thái phê duyệt là PENDING và chưa xuất bản (isPublished = false).
        return movieRepository.save(movie);
    }

    /**
     * Chức năng: Cập nhật thông tin phim
     */
    public Movie updateMovie(Long id, @NonNull Movie movieDetails) {
        // 1. Kiểm tra ID phải được cung cấp (MISSING_MOVIE_ID).
        if (id == null) {
            throw new AppException(ErrorCode.MISSING_MOVIE_ID);
        }

        // 2. Tìm phim hiện có (MOVIE_NOT_FOUND).
        Movie existingMovie = movieRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.MOVIE_NOT_FOUND));

        /* 3. Cập nhật thông tin chi tiết.
         Sử dụng Builder pattern với toBuilder()*/
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
                .status(normalizeStatus(movieDetails.getStatus())) // Đặt lại trạng thái chờ duyệt
                .approveStatus(Approve.PENDING)
                .isPublished(false) // Gỡ xuất bản
                .build();

        // 4. Tự động chuyển trạng thái về PENDING và isPublished = false (yêu cầu duyệt lại)
        return movieRepository.save(updatedMovie);
    }

    /**
     * Chức năng: Cập nhật ảnh banner cho phim.
     */
    public Movie updateBanner(Long id, String banner) {
        Movie movie = movieRepository.findByMovieIDAndIsDeletedFalse(id)
                .orElseThrow(() -> new AppException(ErrorCode.MOVIE_NOT_FOUND));
        movie.setBanner(banner);
        return movieRepository.save(movie);
    }

    /**
     * Chức năng: Xóa mềm phim (Soft Delete).
     */
    public void deleteMovie(Long id){
        // 1. Tìm phim (MOVIE_NOT_FOUND).
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.MOVIE_NOT_FOUND));

        // 2. Kiểm tra nếu phim đã bị xóa trước đó (MOVIE_ALREADY_DELETED).
        if (movie.isDeleted()) {
            throw new AppException(ErrorCode.MOVIE_ALREADY_DELETED); // Tạo thêm error code nếu muốn
        }

        // 3. Đặt isDeleted = true và isPublished = false (gỡ khỏi trang public).
        movie.setDeleted(true);
        movie.setPublished(false);// Đánh dấu là đã xóa
        movieRepository.save(movie); // Lưu lại
    }


    // ===== Admin actions =====
    /**
     * Chức năng (Admin): Phê duyệt phim.
     */
    public Movie approveMovie(Long id) {
        Movie m = movieRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.MOVIE_NOT_FOUND));

        // 1. Kiểm tra phim có bị xóa không (MOVIE_DELETED_OR_INACTIVE).
        if (m.isDeleted()) throw new AppException(ErrorCode.MOVIE_DELETED_OR_INACTIVE);

        // 2. Đặt approveStatus = APPROVE và isPublished = true.
        m.setApproveStatus(Approve.APPROVE);
        m.setPublished(true);
        return movieRepository.save(m);
    }

    /**
     * Chức năng (Admin): Từ chối phê duyệt phim.
     */
    public Movie rejectMovie(Long id) {
        Movie m = movieRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.MOVIE_NOT_FOUND));
        if (m.isDeleted()) throw new AppException(ErrorCode.MOVIE_DELETED_OR_INACTIVE);

        // Đặt approveStatus = DENIED và isPublished = false.
        m.setApproveStatus(Approve.DENIED);
        m.setPublished(false);
        return movieRepository.save(m);
    }
}
