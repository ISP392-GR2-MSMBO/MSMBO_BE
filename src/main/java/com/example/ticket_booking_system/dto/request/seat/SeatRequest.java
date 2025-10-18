package com.example.ticket_booking_system.dto.request.seat;

import com.example.ticket_booking_system.Enum.SeatStatus;
import com.example.ticket_booking_system.entity.Theater;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class SeatRequest {

    @NotNull
    @Schema(description = "ID của ghế cần cập nhật trạng thái")
    private Long seatID;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Mã phòng chiếu (không thể thay đổi qua API này)")
    private Long theaterId;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Hàng ghế (A, B, C...)")
    private String row;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Số ghế trong hàng")
    private Integer number;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Loại ghế (Standard, VIP...)")
    private String type;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Giá ghế")
    private Float price;

    @NotNull(message = "Seat must have a status")
    @Schema(description = "Trạng thái ghế (EMPTY, SOLD, BROKEN...)")
    private SeatStatus status;
}
