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


        return BookingResponse.builder()
                // Thông tin vé
                .bookingID(booking.getBookingID())
                .bookingDate(booking.getBookingDate())
                .status(booking.getStatus())
                .totalPrice(booking.getTotalPrice())
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
                .build();
    }


}
