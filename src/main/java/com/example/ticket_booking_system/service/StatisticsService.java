package com.example.ticket_booking_system.service;

import com.example.ticket_booking_system.dto.reponse.statistic.MonthlyRevenue;
import com.example.ticket_booking_system.dto.reponse.statistic.TopMovie;
import com.example.ticket_booking_system.repository.BookingDetailRepository;
import com.example.ticket_booking_system.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsService {
    private final BookingRepository bookingRepository;
    private final BookingDetailRepository bookingDetailRepository;

    /**
     * API 1: Lấy tổng doanh thu 12 tháng của một năm
     */
    public List<MonthlyRevenue> getYearlyMonthlyRevenue(int year) {

        // 1. Lấy dữ liệu thô từ DB (chỉ các tháng có doanh thu)
        // Gọi hàm mới trong BookingRepository
        List<Object[]> results = bookingRepository.getMonthlyRevenueByYear(year);

        // 2. Chuyển sang Map<Integer, Float> (tháng -> doanh thu)
        Map<Integer, Float> revenueMap = results.stream()
                .collect(Collectors.toMap(
                        res -> (Integer) res[0], // Key: Tháng (Integer)
                        res -> ((Number) res[1]).floatValue() // Value: Doanh thu (Float)
                ));

        // 3. Tạo danh sách 12 tháng (để đảm bảo đủ 12 cột)
        Locale englishLocale = Locale.ENGLISH; // Dùng "Jan", "Feb"
        // Locale vietnameseLocale = new Locale("vi", "VN"); // Dùng "Thg 1", "Thg 2"

        List<MonthlyRevenue> monthlyRevenueList = new ArrayList<>();

        for (int month = 1; month <= 12; month++) {
            // Lấy tên tháng (Jan, Feb, Mar...)
            String monthName = java.time.Month.of(month)
                    .getDisplayName(TextStyle.SHORT, englishLocale);

            // Lấy doanh thu từ Map, nếu không có thì trả về 0.0
            Float revenue = revenueMap.getOrDefault(month, 0.0f);

            monthlyRevenueList.add(new MonthlyRevenue(monthName, revenue));
        }

        return monthlyRevenueList;
    }

    /**
     * API 2: Lấy top phim bán chạy nhất trong tuần này
     */
    public List<TopMovie> getTopMoviesThisWeek() {

        // 1. Xác định ngày bắt đầu và kết thúc của tuần này (Thứ 2 -> Chủ Nhật)
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = today.with(DayOfWeek.SUNDAY);

        // 2. Gọi hàm mới trong BookingDetailRepository
        return bookingDetailRepository.findTopSellingMovies(startOfWeek, endOfWeek);
    }
}
