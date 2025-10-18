package com.example.ticket_booking_system.service;

import com.example.ticket_booking_system.Enum.Approve;
import com.example.ticket_booking_system.entity.Movie;
import com.example.ticket_booking_system.entity.Showtime;
import com.example.ticket_booking_system.entity.Theater;
import com.example.ticket_booking_system.exception.AppException;
import com.example.ticket_booking_system.exception.ErrorCode;
import com.example.ticket_booking_system.repository.MovieRepository;
import com.example.ticket_booking_system.repository.ShowtimeRepository;
import com.example.ticket_booking_system.repository.TheaterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShowtimeService {
    private final ShowtimeRepository showtimeRepository;
    private final TheaterRepository theaterRepository;
    private final MovieRepository movieRepository;
    // ====== Danh sách public cho web (chỉ đã duyệt & publish)
    public List<Showtime> getPublicShowtimes() {
        List<Showtime> list = showtimeRepository.findPublicShowtime(LocalDate.now());
        if (list.isEmpty()) throw new AppException(ErrorCode.SHOWTIME_NOT_FOUND);
        return list;
    }
    // Movie phải CHƯA xóa & ĐÃ publish (đúng flow phê duyệt)
    private Movie requirePublishedActiveMovie(Movie movieRef) {
        Long movieId = (movieRef == null) ? null : movieRef.getMovieID();
        if (movieId == null) throw new AppException(ErrorCode.MOVIE_NOT_FOUND);
        return movieRepository
                .findByMovieIDAndIsDeletedFalseAndIsPublishedTrue(movieId)
                .orElseThrow(() -> new AppException(ErrorCode.MOVIE_DELETED_OR_INACTIVE));
    }

    private boolean isPastShowtime(LocalDate date, LocalTime endTime) {
        LocalDate today = LocalDate.now();
        if (date.isBefore(today)) return true;
        if (date.isEqual(today) && endTime != null) {
            return endTime.isBefore(LocalTime.now());
        }
        return false;
    }
    private Movie requireApprovedActiveMovie(Movie movieRef) {
        if (movieRef == null || movieRef.getMovieID() == null)
            throw new AppException(ErrorCode.MOVIE_NOT_FOUND);

        return movieRepository
                .findByMovieIDAndIsDeletedFalseAndApproveStatus(movieRef.getMovieID(), Approve.APPROVE)
                .orElseThrow(() -> new AppException(ErrorCode.MOVIE_NOT_APPROVED)); // thêm ErrorCode này
    }
    //  Add new showtime
    public Showtime saveShowtime(Showtime showtime) {
        Long theaterId = showtime.getTheater().getTheaterID();
        if (theaterId == null || !theaterRepository.existsById(theaterId)) {
            throw new AppException(ErrorCode.THEATER_NOT_FOUND); //  Lúc này sẽ quăng lỗi
        }
        // 2) Movie phải tồn tại và chưa bị xóa mềm
        if (showtime.getMovie() == null || showtime.getMovie().getMovieID() == null) {
            throw new AppException(ErrorCode.MOVIE_NOT_FOUND);
        }
        Movie movies = requireApprovedActiveMovie(showtime.getMovie());
        Movie movie = movieRepository.findByMovieIDAndIsDeletedFalse(showtime.getMovie().getMovieID())
                .orElseThrow(() -> new AppException(ErrorCode.MOVIE_DELETED_OR_INACTIVE));
        // ép trạng thái về mặc định để staff không tự ý set
        showtime.setApproveStatus(Approve.PENDING);
        showtime.setPublished(false);
        // trùng giờ trong cùng rạp & cùng ngày (overlap)
        boolean conflict = showtimeRepository.existsOverlap(
                showtime.getTheater().getTheaterID(),
                showtime.getDate(),
                showtime.getStartTime(),
                showtime.getEndTime()
        );
        if (conflict) {
            throw new AppException(ErrorCode.SHOWTIME_CONFLICT);
        }
        // ép trạng thái để staff không tự gửi APPROVE từ FE
        showtime.setApproveStatus(Approve.PENDING);
        showtime.setPublished(false);
        return showtimeRepository.save(showtime);
    }

    //  Update new showtime
    public Showtime updateShowtime(Long id, Showtime updatedShowtime) {
        Long theaterId = updatedShowtime.getTheater().getTheaterID();
        if (theaterId == null || !theaterRepository.existsById(theaterId)) {
            throw new AppException(ErrorCode.THEATER_NOT_FOUND); //  Lúc này sẽ quăng lỗi
        }
        Movie movies = requireApprovedActiveMovie(updatedShowtime.getMovie());
        Long movieId = updatedShowtime.getMovie().getMovieID();
        // Movie phải còn hoạt động (chưa bị delete mềm)
        boolean movieActive = movieRepository.existsByMovieIDAndIsDeletedFalse(movieId);
        if (!movieActive) {
            throw new AppException(ErrorCode.MOVIE_DELETED_OR_INACTIVE);
        }
        Showtime existing = showtimeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SHOWTIME_NOT_FOUND));
        if (existing.isPublished())
            throw new AppException(ErrorCode.CANNOT_EDIT_PUBLISHED);
        existing.setTheater(updatedShowtime.getTheater());
        existing.setMovie(updatedShowtime.getMovie());
        existing.setDate(updatedShowtime.getDate());
        existing.setStartTime(updatedShowtime.getStartTime());
        existing.setEndTime(updatedShowtime.getEndTime());
        // sửa là quay về chờ duyệt & tắt publish (đề phòng trước đó là DENIED)
        existing.setApproveStatus(Approve.PENDING);
        existing.setPublished(false);
        boolean conflict = showtimeRepository.existsOverlap(
                existing.getTheater().getTheaterID(),
                existing.getDate(),
                existing.getStartTime(),
                existing.getEndTime()
                //existing.getShowtimeID()
        );
        if (conflict) {
            throw new AppException(ErrorCode.SHOWTIME_CONFLICT);
        }
        return showtimeRepository.save(existing);
    }
    // ===== Admin approve / deny =====
    public Showtime adminApproveOrDeny(Long id, boolean approve) {
        Showtime s = showtimeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SHOWTIME_NOT_FOUND));
        // Re-validate: phim vẫn phải đang published & chưa xóa (phòng TH phim bị unpublish sau khi tạo showtime)
        // Không cho publish suất chiếu quá khứ
        if (approve) {
            requirePublishedActiveMovie(s.getMovie());
            if (isPastShowtime(s.getDate(), s.getEndTime())) {
                throw new AppException(ErrorCode.PAST_SHOWTIME_CANNOT_PUBLISH);
            }
            s.setApproveStatus(Approve.APPROVE);
            s.setPublished(true);
        } else {
            s.setApproveStatus(Approve.DENIED);
            s.setPublished(false);
        }
        return showtimeRepository.save(s);
    }
    //  Delete showtime
    public void deleteShowtime(Long id) {
        if (!showtimeRepository.existsById(id)) {
            throw new AppException(ErrorCode.SHOWTIME_NOT_FOUND);
        }
        showtimeRepository.deleteById(id);
    }
}
