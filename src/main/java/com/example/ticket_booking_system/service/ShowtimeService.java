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
        List<Showtime> showtimes = showtimeRepository.findByMovieID(movieId);
        if (showtimes.isEmpty()) {
            throw new AppException(ErrorCode.SHOWTIME_NOT_FOUND);
        }
        return showtimes;
    }
    //  Lấy showtime theo ID
    public Showtime getShowtimeById(Long id) {
        return showtimeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SHOWTIME_NOT_FOUND));
    }
    //  Add new showtime
    public Showtime saveShowtime(Showtime showtime) {
        return showtimeRepository.save(showtime);
    }

    //  Update new showtime
    public Showtime updateShowtime(Long id, Showtime updatedShowtime) {
        Showtime existing = showtimeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SHOWTIME_NOT_FOUND));

        existing.setTheaterID(updatedShowtime.getTheaterID());
        existing.setMovieID(updatedShowtime.getMovieID());
        existing.setStartTime(updatedShowtime.getStartTime());
        existing.setEndTime(updatedShowtime.getEndTime());

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
