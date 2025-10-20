package com.example.ticket_booking_system.mapper;

import com.example.ticket_booking_system.dto.reponse.showtime.ShowtimeResponse;
import com.example.ticket_booking_system.dto.request.showtime.ShowtimeRequest;
import com.example.ticket_booking_system.entity.Movie;
import com.example.ticket_booking_system.entity.Showtime;
import com.example.ticket_booking_system.entity.Theater;

public class ShowtimeMapper {
    public static Showtime toEntity(ShowtimeRequest request) {
        Showtime showtime = new Showtime();
        // Gán movieId qua quan hệ ManyToOne
        Movie movie = new Movie();
        movie.setMovieID(request.getMovieID());
        showtime.setMovie(movie);

        // Gán theaterId qua quan hệ ManyToOne
        Theater theater = new Theater();
        theater.setTheaterID(request.getTheaterID());
        showtime.setTheater(theater);

        // Thời gian
        showtime.setDate(request.getDate());
        showtime.setStartTime(request.getStartTime());
        showtime.setEndTime(request.getEndTime());
        return showtime;
    }

    public static ShowtimeResponse toResponse(Showtime entity) {
        ShowtimeResponse response = new ShowtimeResponse();
        response.setShowtimeID(entity.getShowtimeID());
        response.setMovieID(entity.getMovie().getMovieID());
        response.setTheaterID(entity.getTheater().getTheaterID());
        response.setDate(entity.getDate());
        response.setStartTime(entity.getStartTime());
        response.setEndTime(entity.getEndTime());
        response.setApproveStatus(entity.getApproveStatus());
        return response;
    }
}
