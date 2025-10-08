package com.example.ticket_booking_system.dto.reponse.showtime;

import java.time.LocalDateTime;

public class ShowtimeResponse {
    private Integer showtimeID;
    private String theaterID;
    private Integer movieID;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public ShowtimeResponse() {
    }

    public ShowtimeResponse(Integer showtimeID, String theaterID, Integer movieID, LocalDateTime startTime, LocalDateTime endTime) {
        this.showtimeID = showtimeID;
        this.theaterID = theaterID;
        this.movieID = movieID;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Integer getShowtimeID() {
        return showtimeID;
    }

    public void setShowtimeID(Integer showtimeID) {
        this.showtimeID = showtimeID;
    }

    public String getTheaterID() {
        return theaterID;
    }

    public void setTheaterID(String theaterID) {
        this.theaterID = theaterID;
    }

    public Integer getMovieID() {
        return movieID;
    }

    public void setMovieID(Integer movieID) {
        this.movieID = movieID;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
