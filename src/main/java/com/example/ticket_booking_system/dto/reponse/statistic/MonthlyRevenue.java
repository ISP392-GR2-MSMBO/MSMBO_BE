package com.example.ticket_booking_system.dto.reponse.statistic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyRevenue {
    private String month;
    private Float revenue;
}
