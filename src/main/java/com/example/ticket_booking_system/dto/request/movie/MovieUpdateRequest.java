package com.example.ticket_booking_system.dto.request.movie;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieUpdateRequest {
    @NotBlank(message = "Movie name cannot be blank")
    private String movieName;
    @Size(min = 1, max = 100, message = "Genre requires at least 1 character and maximum 100 characters")
    private String genre;
    @Min(value = 1, message = "Duration must be greater than 0 minutes")
    private int duration;
    @Size(max = 20, message = "Maximum 20 character limit")
    private String age;
    @Size(min = 1, message = "Director requires at least 1 character")
    private String director;
    @Size(min = 1, message = "Actress requires at least 1 character")
    private String actress;

    // ĐÂY LÀ KHÁC BIỆT QUAN TRỌNG:
    // Đã xóa @FutureOrPresent
    @NotNull(message = "Release date cannot be null")
    private LocalDate releaseDate;

    private String language;
    private String description;
    private String poster;
    private String trailer;
    private String status;
}
