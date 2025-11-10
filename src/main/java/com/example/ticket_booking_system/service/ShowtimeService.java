package com.example.ticket_booking_system.service;

import com.example.ticket_booking_system.Enum.Approve;
import com.example.ticket_booking_system.entity.Movie;
import com.example.ticket_booking_system.entity.Showtime;
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

    //Danh sách public cho web (chỉ đã duyệt & publish)
    //Sắp xếp theo ngày và giờ bắt đầu.
    public List<Showtime> getAllShowtimes() {
        List<Showtime> list = showtimeRepository.findAllShowtime();
        if (list.isEmpty()) throw new AppException(ErrorCode.SHOWTIME_NOT_FOUND);
        return list;
    }


    /**
     * Chức năng: Lấy các suất chiếu public (đã duyệt, chưa xóa, chưa chiếu) của 1 phim.
     * Logic:
     * 2. Chỉ lấy các suất chưa bị xóa mềm (isDeleted = false).
     * Public showtime theo movieId (flow chuẩn, có kiểm tra APPROVE)
     */
    public List<Showtime> getPublicShowtimesByMovieId(Long movieId) {
        if (movieId == null) throw new AppException(ErrorCode.MOVIE_NOT_FOUND);
        // 1. Chỉ lấy các suất chiếu từ ngày hôm nay trở về sau.
        List<Showtime> showtime = showtimeRepository.findByMovie_MovieIDAndIsDeletedFalseAndDateGreaterThanEqualOrderByDateAscStartTimeAsc(
                movieId, LocalDate.now());
        if (showtime.isEmpty()) throw new AppException(ErrorCode.SHOWTIME_NOT_FOUND);
        return showtime;
    }

    /**
     * Chức năng nội bộ: Kiểm tra xem phim có "Hợp lệ để xuất bản" không.
     * Logic: Phim phải chưa bị xóa (isDeleted = false) VÀ ĐÃ publish (isPublished = true).
     */
    private Movie requirePublishedActiveMovie(Movie movieRef) {
        Long movieId = (movieRef == null) ? null : movieRef.getMovieID();
        if (movieId == null) throw new AppException(ErrorCode.MOVIE_NOT_FOUND);
        return movieRepository
                .findByMovieIDAndIsDeletedFalseAndIsPublishedTrue(movieId)
                .orElseThrow(() -> new AppException(ErrorCode.MOVIE_DELETED_OR_INACTIVE));
    }

    /**
     * Chức năng nội bộ: Kiểm tra xem suất chiếu đã diễn ra hay chưa.
     */
    private boolean isPastShowtime(LocalDate date, LocalTime endTime) {
        LocalDate today = LocalDate.now();
        if (date.isBefore(today)) return true; // Nếu là ngày trong quá khứ -> true
        if (date.isEqual(today) && endTime != null) {
            // Nếu là ngày hôm nay, kiểm tra giờ kết thúc đã qua chưa
            return endTime.isBefore(LocalTime.now());
        }
        return false;
    }

    /**
     * Chức năng nội bộ: Kiểm tra xem phim có "Hợp lệ để tạo suất chiếu" không.
     * Logic: Phim phải chưa bị xóa VÀ phải được Admin phê duyệt (APPROVE).
     */
    private Movie requireApprovedActiveMovie(Movie movieRef) {
        if (movieRef == null || movieRef.getMovieID() == null)
            throw new AppException(ErrorCode.MOVIE_NOT_FOUND);

        return movieRepository
                .findByMovieIDAndIsDeletedFalseAndApproveStatus(movieRef.getMovieID(), Approve.APPROVE)
                .orElseThrow(() -> new AppException(ErrorCode.MOVIE_NOT_APPROVED)); // thêm ErrorCode này
    }

    /**
     * Chức năng: Thêm một suất chiếu mới (cho Staff).
     */
    //  Add new showtime
    public Showtime saveShowtime(Showtime showtime) {
        // 1. Kiểm tra rạp (Theater) phải tồn tại (THEATER_NOT_FOUND).
        Long theaterId = showtime.getTheater().getTheaterID();
        if (theaterId == null || !theaterRepository.existsById(theaterId)) {
            throw new AppException(ErrorCode.THEATER_NOT_FOUND); //  Lúc này sẽ quăng lỗi
        }

        // 2) Movie phải tồn tại và chưa bị xóa mềm và đã được Admin duyệt
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

    /**
     * Chức năng: Cập nhật một suất chiếu (cho Staff).
     */
    //  Update new showtime
    public Showtime updateShowtime(Long id, Showtime updatedShowtime) {
        // 1. kiểm tra rạp (THEATER_NOT_FOUND) và phim (requireApprovedActiveMovie).
        Long theaterId = updatedShowtime.getTheater().getTheaterID();
        if (theaterId == null || !theaterRepository.existsById(theaterId)) {
            throw new AppException(ErrorCode.THEATER_NOT_FOUND);
        }
        Movie movies = requireApprovedActiveMovie(updatedShowtime.getMovie());
        Long movieId = updatedShowtime.getMovie().getMovieID();

        // Movie phải còn hoạt động (chưa bị delete mềm)
        boolean movieActive = movieRepository.existsByMovieIDAndIsDeletedFalse(movieId);
        if (!movieActive) {
            throw new AppException(ErrorCode.MOVIE_DELETED_OR_INACTIVE);
        }

        // 2. Tìm suất chiếu hiện có (SHOWTIME_NOT_FOUND).
        Showtime existing = showtimeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SHOWTIME_NOT_FOUND));

        // 3. Nếu suất chiếu đã xuất bản (isPublished = true) -> không cho sửa (CANNOT_EDIT_PUBLISHED).
        if (existing.isPublished())
            throw new AppException(ErrorCode.CANNOT_EDIT_PUBLISHED);

        // 4. Cập nhật thông tin.
        existing.setTheater(updatedShowtime.getTheater());
        existing.setMovie(updatedShowtime.getMovie());
        existing.setDate(updatedShowtime.getDate());
        existing.setStartTime(updatedShowtime.getStartTime());
        existing.setEndTime(updatedShowtime.getEndTime());
        // sửa là quay về chờ duyệt & và isPublished = false (yêu cầu duyệt lại) (đề phòng trước đó là DENIED)
        existing.setApproveStatus(Approve.PENDING);
        existing.setPublished(false);
        boolean conflict = showtimeRepository.existsOverlap(
                existing.getTheater().getTheaterID(),
                existing.getDate(),
                existing.getStartTime(),
                existing.getEndTime()
                //existing.getShowtimeID()
        );

        // 6. Kiểm tra lại trùng lặp thời gian (SHOWTIME_CONFLICT).
        if (conflict) {
            throw new AppException(ErrorCode.SHOWTIME_CONFLICT);
        }
        return showtimeRepository.save(existing);
    }

    /**
     * Lấy suất chiếu public theo MovieID và Lọc theo Tên Rạp (2D/IMAX)
     */
    public List<Showtime> getPublicShowtimesByMovieAndTheaterName(Long movieId, String theaterName) {
        if (movieId == null) throw new AppException(ErrorCode.MOVIE_NOT_FOUND);

        List<Showtime> showtimes = showtimeRepository
                .findByMovie_MovieIDAndTheater_TheaterNameAndIsDeletedFalseAndDateGreaterThanEqualOrderByDateAscStartTimeAsc(
                        movieId, theaterName, LocalDate.now()
                );

        // Không ném lỗi nếu rạp đó không có suất, chỉ trả về danh sách rỗng
        // if (showtimes.isEmpty()) throw new AppException(ErrorCode.SHOWTIME_NOT_FOUND);

        return showtimes;
    }

    /**
     * Chức năng: Admin phê duyệt (approve = true) hoặc từ chối (approve = false) suất chiếu.
     * Logic khi Phê duyệt (approve = true):
     */
    //Admin approve / deny
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

            // 3. Đặt trạng thái: APPROVE và isPublished = true.
            s.setApproveStatus(Approve.APPROVE);
            s.setPublished(true);
        } else {
            // Đặt trạng thái: DENIED và isPublished = false.
            s.setApproveStatus(Approve.DENIED);
            s.setPublished(false);
        }
        return showtimeRepository.save(s);
    }
    //  Delete showtime
//    public void deleteShowtime(Long id) {
//        if (!showtimeRepository.existsById(id)) {
//            throw new AppException(ErrorCode.SHOWTIME_NOT_FOUND);
//        }
//        showtimeRepository.deleteById(id);
//    }

    /**
     * Chức năng: Xóa mềm suất chiếu (Soft Delete)
     */
    // Xóa movie
    public void deleteShowtime(Long id){
        // 1. Tìm suất chiếu (SHOWTIME_NOT_FOUND).
        Showtime showtime = showtimeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SHOWTIME_NOT_FOUND));

        // 2. Kiểm tra nếu đã xóa rồi thì báo lỗi (SHOWTIME_ALREADY_DELETED).
        if (showtime.isDeleted()) {
            throw new AppException(ErrorCode.SHOWTIME_ALREADY_DELETED); // Tạo thêm error code nếu muốn
        }

        // 3. Đặt isDeleted = true và isPublished = false (gỡ khỏi trang public).
        showtime.setDeleted(true);
        showtime.setPublished(false);// Đánh dấu là đã xóa
        showtimeRepository.save(showtime); // Lưu lại
    }
}
