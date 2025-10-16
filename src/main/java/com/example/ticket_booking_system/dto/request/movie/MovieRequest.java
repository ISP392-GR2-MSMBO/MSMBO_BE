package com.example.ticket_booking_system.dto.request.movie;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieRequest {
    @NotBlank(message = "Movie name cannot be blank")
    //@Size(min = 1, max = 255, message = "Tên phim cần ít nhất 1 ký tự và tối đa 255 ký tự")
    private String movieName;
    @Size(min = 1, max = 50, message = "Genre requires at least 1 character and maximum 50 characters")
    private String genre;
    @Min(value = 1, message = "Duration must be greater than 0 minutes")
    private int duration;
    @Size(max = 20, message = "Giới hạn độ tuổi tối đa 20 ký tự")
    private String age;
    @Size(min = 1, max = 40, message = "Director requires at least 1 character and maximum 40 characters")
    private String director;
    @Size(min = 1, max = 40, message = "Actress requires at least 1 character and maximum 40 characters")
    private String actress;
    @NotNull(message = "Release date cannot be null")
    @FutureOrPresent(message = "Date must be today or in the future")
    private LocalDate releaseDate;
    private String language;
    private String description;
    private String status;
}


