package com.example.ticket_booking_system.dto.reponse.statistic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopMovie {
    private String movieName;
    private Long ticketCount;
}
