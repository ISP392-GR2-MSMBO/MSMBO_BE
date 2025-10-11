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
    @Size(min = 1, max = 100, message = "Thể loại cần ít nhất 1 ký tự và tối đa 100 ký tự")
    private String genre;
    @Min(value = 1, message = "Duration must be greater than 0 minutes")
    private int duration;
    @Size(max = 20, message = "Giới hạn độ tuổi tối đa 20 ký tự")
    private String age;
    private String director;
    private String actress;
    @NotNull(message = "Release date cannot be null")
    private LocalDate releaseDate;
    private String language;
    private String description;
    private String status;
}


