package com.example.ticket_booking_system.dto.reponse.movie;

import com.example.ticket_booking_system.Enum.Approve;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieResponse {
    private Long movieID;
    private String movieName;
    private String genre;
    private int duration;
    private String age;
    private String director;
    private String actress;
    private LocalDate releaseDate;
    private String language;
    private String description;
    private String poster;
    private String trailer;
    private String status;
    private Approve approveStatus;
}


