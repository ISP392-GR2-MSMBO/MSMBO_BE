package com.example.ticket_booking_system.mapper;

import com.example.ticket_booking_system.dto.reponse.movie.MovieResponse;
import com.example.ticket_booking_system.dto.request.movie.MovieRequest;
import com.example.ticket_booking_system.entity.Movie;

public class MovieMapper {
    public static Movie toEntity(MovieRequest request) {
        Movie movie = new Movie();
        movie.setMovieName(request.getMovieName());
        movie.setGenre(request.getGenre());
        movie.setDuration(request.getDuration());
        movie.setAge(request.getAge());
        movie.setDirector(request.getDirector());
        movie.setActress(request.getActress());
        movie.setReleaseDate(request.getReleaseDate());
        movie.setLanguage(request.getLanguage());
        movie.setDescription(request.getDescription());
        movie.setPoster(request.getPoster());
        movie.setTrailer(request.getTrailer());
        movie.setBanner(request.getBanner());
        movie.setStatus(request.getStatus());
        return movie;
    }

    public static MovieResponse toResponse(Movie movie) {
        MovieResponse res = new MovieResponse();
        res.setMovieID(movie.getMovieID());
        res.setMovieName(movie.getMovieName());
        res.setGenre(movie.getGenre());
        res.setDuration(movie.getDuration());
        res.setAge(movie.getAge());
        res.setDirector(movie.getDirector());
        res.setActress(movie.getActress());
        res.setReleaseDate(movie.getReleaseDate());
        res.setLanguage(movie.getLanguage());
        res.setDescription(movie.getDescription());
        res.setPoster(movie.getPoster());
        res.setTrailer(movie.getTrailer());
        res.setBanner(movie.getBanner());
        res.setStatus(movie.getStatus());
        res.setApproveStatus(movie.getApproveStatus());
        res.setDeleted(movie.isDeleted());
        return res;
    }
}