package com.example.ticket_booking_system.service;

import com.example.ticket_booking_system.entity.Showtime;
import com.example.ticket_booking_system.exception.AppException;
import com.example.ticket_booking_system.exception.ErrorCode;
import com.example.ticket_booking_system.repository.ShowtimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShowtimeService {
    private final ShowtimeRepository showtimeRepository;

    @Autowired
    public ShowtimeService(ShowtimeRepository showtimeRepository) {
        this.showtimeRepository = showtimeRepository;
    }

    //  List all showtime
    public List<Showtime> getAllShowtimes() {
        return showtimeRepository.findAll();
    }
    
    //  Find by movie ID (used by controller /showtimes/search?movieId=)
    public List<Showtime> findByMovieId(Integer movieId) {
        return showtimeRepository.findByMovieID(movieId);
    }
    

    //  showtime ID
    public Showtime getShowtimeById(Integer id) {
        return showtimeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SHOWTIME_NOT_FOUND));
    }

    //  Add new showtime
    public Showtime saveShowtime(Showtime showtime) {
        return showtimeRepository.save(showtime);
    }

    //  Update new showtime
    public Showtime updateShowtime(Integer id, Showtime updatedShowtime) {
        Showtime existing = showtimeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SHOWTIME_NOT_FOUND));

        existing.setTheaterID(updatedShowtime.getTheaterID());
        existing.setMovieID(updatedShowtime.getMovieID());
        existing.setStartTime(updatedShowtime.getStartTime());
        existing.setEndTime(updatedShowtime.getEndTime());

        return showtimeRepository.save(existing);
    }

    //  Delete showtime
    public void deleteShowtime(Integer id) {
        if (!showtimeRepository.existsById(id)) {
            throw new AppException(ErrorCode.SHOWTIME_NOT_FOUND);
        }
        showtimeRepository.deleteById(id);
    }
}
