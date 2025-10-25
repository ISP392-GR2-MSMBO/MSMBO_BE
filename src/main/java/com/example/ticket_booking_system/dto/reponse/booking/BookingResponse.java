package com.example.ticket_booking_system.dto.reponse.booking;

import com.example.ticket_booking_system.Enum.BookingStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
public class BookingResponse {

    // --- Thông tin vé ---
    private Long bookingID; // Mã vé
    private LocalDate bookingDate; // Ngày đặt
    private BookingStatus status;
    private Float totalPrice; // Tổng tiền cuối cùng

//    // --- Thông tin người đặt ---
//    private String fullName;
//    private String email;
//    private String phone;

    // --- Thông tin suất chiếu ---
    private String movieName;
    private String theaterName;
    private LocalDate showDate;
    private LocalTime startTime;

    // --- Chi tiết vé ---
    private List<BookingDetailResponse> seats; // Danh sách ghế
    private List<BookingComboDetailResponse> combos; // Danh sách combo
}
