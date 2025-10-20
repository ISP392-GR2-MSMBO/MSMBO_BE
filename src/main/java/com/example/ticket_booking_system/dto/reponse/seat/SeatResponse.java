package com.example.ticket_booking_system.dto.reponse.seat;

import com.example.ticket_booking_system.entity.Theater;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatResponse {
    private Long seatID;
    private Long theaterId;
    private String row;
    private Integer number;
    private String type;
    private Float price;

    private Float basePrice;
    private Float finalePrice;
    private String promotionName;

    private String status;
}
