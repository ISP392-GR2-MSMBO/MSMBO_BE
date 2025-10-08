package com.example.ticket_booking_system.mapper;

import com.example.ticket_booking_system.dto.reponse.showtime.ShowtimeResponse;
import com.example.ticket_booking_system.dto.request.showtime.ShowtimeRequest;
import com.example.ticket_booking_system.entity.Showtime;

public class ShowtimeMapper {
    public static Showtime toEntity(ShowtimeRequest request) {
        Showtime showtime = new Showtime();
        showtime.setTheaterID(request.getTheaterID());
        showtime.setMovieID(request.getMovieID());
        showtime.setStartTime(request.getStartTime());
        showtime.setEndTime(request.getEndTime());
        return showtime;
    }

    public static ShowtimeResponse toResponse(Showtime entity) {
        ShowtimeResponse response = new ShowtimeResponse();
        response.setShowtimeID(entity.getShowtimeID());
        response.setTheaterID(entity.getTheaterID());
        response.setMovieID(entity.getMovieID());
        response.setStartTime(entity.getStartTime());
        response.setEndTime(entity.getEndTime());
        return response;
    }
}
