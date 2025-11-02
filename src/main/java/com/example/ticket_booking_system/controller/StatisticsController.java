package com.example.ticket_booking_system.controller;

import com.example.ticket_booking_system.dto.reponse.statistic.MonthlyRevenue;
import com.example.ticket_booking_system.dto.reponse.statistic.TopMovie;
import com.example.ticket_booking_system.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:3000")
@CrossOrigin(origins = "https://chillcinema.netlify.app/") // cần thêm khi nối React
public class StatisticsController {
    private final StatisticsService statisticsService;

    /**
     * API 1 (Cho Manager): Lấy doanh thu 12 tháng (biểu đồ cột)
     * Cách gọi: GET /api/statistics/monthly-revenue?year=2025
     */
    @GetMapping("/monthly-revenue")
    public ResponseEntity<List<MonthlyRevenue>> getMonthlyRevenue(
            @RequestParam(required = false, defaultValue = "0") int year
    ) {
        // Nếu không truyền năm (year=0), tự động lấy năm hiện tại
        int targetYear = (year == 0) ? LocalDate.now().getYear() : year;

        List<MonthlyRevenue> response = statisticsService.getYearlyMonthlyRevenue(targetYear);
        return ResponseEntity.ok(response);
    }

    /**
     * API 2 (Cho Staff): Lấy top phim bán chạy tuần này
     * Cách gọi: GET /api/statistics/top-movies-week
     */
    @GetMapping("/top-movies-week")
    public ResponseEntity<List<TopMovie>> getTopMoviesWeek() {
        List<TopMovie> response = statisticsService.getTopMoviesThisWeek();
        return ResponseEntity.ok(response);
    }
}
