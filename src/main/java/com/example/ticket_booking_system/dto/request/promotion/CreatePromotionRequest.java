package com.example.ticket_booking_system.dto.request.promotion;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CreatePromotionRequest {

    @NotBlank(message = "Promotion name must not empty")
    private String name;

    @NotNull(message = "Start Date must not empty")
    @FutureOrPresent(message = "Start Date must start from today or in the future")
    private LocalDate startDate;

    @NotNull(message = "End Date must not empty")
    @FutureOrPresent(message = "End Date must start from today or in the future")
    private LocalDate endDate;

    @NotBlank(message = "Discount Type must not empty")
    private String discountType; // "percentage" hoặc "fixed_amount"

    @NotNull(message = "Discount Value must not empty")
    private Float discountValue;
}
