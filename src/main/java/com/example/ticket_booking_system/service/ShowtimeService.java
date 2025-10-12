package com.example.ticket_booking_system.service;

import com.example.ticket_booking_system.entity.Showtime;
import com.example.ticket_booking_system.exception.AppException;
import com.example.ticket_booking_system.exception.ErrorCode;
import com.example.ticket_booking_system.repository.ShowtimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShowtimeService {
    private final ShowtimeRepository showtimeRepository;
    //  List all showtime
    public List<Showtime> getAllShowtimes() {
        List<Showtime> showtimes = showtimeRepository.findAll();
        if (showtimes.isEmpty()) {
            throw new AppException(ErrorCode.SHOWTIME_NOT_FOUND); // hoặc tạo lỗi mới SHOWTIME_EMPTY_LIST
        }
        return showtimes;
    }
    
    //  Find by movie ID (used by controller /showtimes/search?movieId=)
    public List<Showtime> findByMovieId(Long movieId) {
        List<Showtime> showtimes = showtimeRepository.findByMovie_MovieID(movieId);
        if (showtimes.isEmpty()) {
            throw new AppException(ErrorCode.SHOWTIME_NOT_FOUND);
        }
        return showtimes;
    }
    public List<Showtime> findByMovieName(String keyword) {
        var list = showtimeRepository.findByMovie_MovieNameContainingIgnoreCase(keyword);
        if (list.isEmpty()) {
            throw new AppException(ErrorCode.SHOWTIME_NOT_FOUND);
        }
        return list;
    }
    //  Lấy showtime theo ID
    public Showtime getShowtimeById(Long id) {
        return showtimeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SHOWTIME_NOT_FOUND));
    }
    //  Add new showtime
    public Showtime saveShowtime(Showtime showtime) {
        if (showtime.getTheater() == null || showtime.getTheater().getTheaterID() == null) {
            throw new AppException(ErrorCode.THEATER_NOT_FOUND);
        }
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

        return showtimeRepository.save(showtime);
    }

    //  Update new showtime
    public Showtime updateShowtime(Long id, Showtime updatedShowtime) {
        Showtime existing = showtimeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SHOWTIME_NOT_FOUND));

        existing.setTheater(updatedShowtime.getTheater());
        existing.setMovie(updatedShowtime.getMovie());
        existing.setDate(updatedShowtime.getDate());
        existing.setStartTime(updatedShowtime.getStartTime());
        existing.setEndTime(updatedShowtime.getEndTime());
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

    //  Delete showtime
    public void deleteShowtime(Long id) {
        if (!showtimeRepository.existsById(id)) {
            throw new AppException(ErrorCode.SHOWTIME_NOT_FOUND);
        }
        showtimeRepository.deleteById(id);
    }
}
