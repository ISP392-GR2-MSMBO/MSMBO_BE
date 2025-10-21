package com.example.ticket_booking_system.dto.request.promotion;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "Apply one promotion to many seats at the same time")
public class ApplyPromotionRequest {

    @Schema(description = "List of seatTypeID need promotion", example = "[1, 3]", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "Seat Type ID must not empty")
    private List<Long> seatTypeID;

}
