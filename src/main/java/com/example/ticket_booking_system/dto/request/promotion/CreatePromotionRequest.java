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

    @NotBlank(message = "Tên khuyến mãi không được để trống")
    private String name;

    @NotNull(message = "Ngày bắt đầu không được để trống")
    @FutureOrPresent(message = "Ngày bắt đầu phải là hôm nay hoặc trong tương lai")
    private LocalDate startDate;

    @NotNull(message = "Ngày kết thúc không được để trống")
    @FutureOrPresent(message = "Ngày kết thúc phải là hôm nay hoặc trong tương lai")
    private LocalDate endDate;

    @NotBlank(message = "Loại giảm giá không được để trống")
    private String discountType; // "percentage" hoặc "fixed_amount"

    @NotNull(message = "Giá trị giảm giá không được để trống")
    private Float discountValue;
}
