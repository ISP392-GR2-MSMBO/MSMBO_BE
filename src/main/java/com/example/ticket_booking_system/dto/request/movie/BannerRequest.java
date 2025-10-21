package com.example.ticket_booking_system.dto.request.movie;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BannerRequest {
    @NotBlank(message = "Banner URL cannot be empty")
    private String banner;
}
