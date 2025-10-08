package com.example.ticket_booking_system.dto.request;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class MovieRequest {
    private String movieName;
    private String genre;
    private int duration;
    private String age;
    private String director;
    private String actress;
    private LocalDate releaseDate;
    private String language;
    private String description;
    public String getMovieName() { return movieName; }
    public void setMovieName(String movieName) { this.movieName = movieName; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public String getAge() { return age; }
    public void setAge(String age) { this.age = age; }

    public String getDirector() { return director; }
    public void setDirector(String director) { this.director = director; }

    public String getActress() { return actress; }
    public void setActress(String actress) { this.actress = actress; }

    public LocalDate getReleaseDate() { return releaseDate; }
    public void setReleaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}


