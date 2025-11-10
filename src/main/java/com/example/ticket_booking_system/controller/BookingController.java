package com.example.ticket_booking_system.controller;

import com.example.ticket_booking_system.dto.reponse.booking.BookingDetailResponse;
import com.example.ticket_booking_system.dto.reponse.booking.BookingResponse;
import com.example.ticket_booking_system.dto.reponse.seat.SeatResponse;
import com.example.ticket_booking_system.dto.request.booking.CreateBookingRequest;
import com.example.ticket_booking_system.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@CrossOrigin(origins = "https://chillcinema.vercel.app/") // cần thêm khi nối React
public class BookingController {

    private final BookingService bookingService;

    /**
     * API 1: Tạo booking (đặt vé)
     */
    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(
            @PathVariable Long userId,
            @Valid @RequestBody CreateBookingRequest request
    ) {
        BookingResponse response = bookingService.createBooking(userId, request); // <-- SỬA Ở ĐÂY
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * API 2: Lấy TẤT CẢ ghế (Sơ đồ ghế) của 1 suất chiếu
     */
    @GetMapping("/showtime/{showtimeId}/seats")
    public ResponseEntity<List<SeatResponse>> getSeatMap(@PathVariable Long showtimeId) {
        return ResponseEntity.ok(bookingService.getSeatMapForShowtime(showtimeId));
    }

    /**
     * API 3 (MỚI): Lấy CHỈ những ghế ĐÃ BÁN của 1 suất chiếu
     */
    @GetMapping("/showtime/{showtimeId}/sold-seats")
    public ResponseEntity<List<SeatResponse>> getSoldSeats(@PathVariable Long showtimeId) {
        return ResponseEntity.ok(bookingService.getSoldSeatsForShowtime(showtimeId));
    }

    /**
     * API 4: Lấy LỊCH SỬ đặt vé của 1 user
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookingResponse>> getBookingsByUser(@PathVariable Long userId) {
        List<BookingResponse> responses = bookingService.getBookingsByUserId(userId);
        return ResponseEntity.ok(responses);
    }

    /**
     * API 5: Lấy chi tiết 1 vé (Booking)
     */
    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingResponse> getBookingById(@PathVariable Long bookingId) {
        BookingResponse response = bookingService.getBookingById(bookingId);
        return ResponseEntity.ok(response);
    }

    /**
     * API 6: Lấy chi tiết 1 ghế (BookingDetail)
     */
    @GetMapping("/details/{bookingDetailId}")
    public ResponseEntity<BookingDetailResponse> getBookingDetailById(@PathVariable Long bookingDetailId) {
        BookingDetailResponse response = bookingService.getBookingDetailById(bookingDetailId);
        return ResponseEntity.ok(response);
    }

    /**
     * API 7: Xóa Booking (Dùng khi người dùng hủy thanh toán)
     * Frontend sẽ gọi API này khi PayOS redirect về trang "cancelUrl"
     */
    @DeleteMapping("/{bookingId}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long bookingId) {
        bookingService.hardDeleteBooking(bookingId);
        return ResponseEntity.noContent().build();
    }
}