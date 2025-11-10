package com.example.ticket_booking_system.mapper;

import com.example.ticket_booking_system.dto.reponse.booking.BookingDetailResponse;
import com.example.ticket_booking_system.dto.reponse.booking.BookingResponse;
import com.example.ticket_booking_system.entity.*;

import java.util.List;
import java.util.stream.Collectors;

public class BookingMapper {

    // Hàm chính: Chuyển Booking Entity -> BookingResponse DTO
    public static BookingResponse toBookingResponse(Booking booking) {
        User user = booking.getUser();
        Showtime showtime = booking.getShowtime();
        Movie movie = showtime.getMovie();
        Theater theater = showtime.getTheater();

        // Map chi tiết ghế
        List<BookingDetailResponse> seatResponses = booking.getBookingDetails()
                .stream()
                .map(BookingMapper::toBookingDetailResponse)
                .collect(Collectors.toList());

        // --- BẮT ĐẦU TÌM PROMOTION ID ---
        Long appliedPromoId = null;
        if (booking.getBookingDetails() != null) {
            // Lấy ID của KM đầu tiên tìm thấy trong danh sách ghế
            // (Giả định tất cả các ghế trong 1 vé dùng chung 1 KM nếu có)
            for (BookingDetail detail : booking.getBookingDetails()) {
                if (detail.getAppliedPromotion() != null) {
                    appliedPromoId = detail.getAppliedPromotion().getPromotionID();
                    break; // Lấy cái đầu tiên rồi dừng lại
                }
            }
        }
        return BookingResponse.builder()
                // Thông tin vé
                .bookingID(booking.getBookingID())
                .bookingDate(booking.getBookingDate())
                .status(booking.getStatus())
                .totalPrice(booking.getTotalPrice())
                .promotionID(appliedPromoId)
                // Thông tin người đặt
//                .fullName(user.getFullName())
//                .email(user.getEmail())
//                .phone(user.getPhone())
                // Thông tin suất chiếu
                .movieName(movie.getMovieName())
                .theaterName(theater.getTheaterName())
                .showDate(showtime.getDate())
                .startTime(showtime.getStartTime())
                // Chi tiết
                .seats(seatResponses)
                .build();
    }

    // Hàm phụ: Map chi tiết 1 ghế
    public static BookingDetailResponse toBookingDetailResponse(BookingDetail detail) {
        return BookingDetailResponse.builder()
                .seatRow(detail.getSeat().getRow())
                .seatNumber(detail.getSeat().getNumber())
                .price(detail.getPrice())
                .status(detail.getStatus())
                .build();
    }


}
