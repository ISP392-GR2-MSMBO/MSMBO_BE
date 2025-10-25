package com.example.ticket_booking_system.service;

import com.example.ticket_booking_system.Enum.BookingStatus;
import com.example.ticket_booking_system.Enum.SeatStatus;
import com.example.ticket_booking_system.dto.reponse.booking.BookingResponse;
import com.example.ticket_booking_system.dto.request.booking.BookingComboRequest;
import com.example.ticket_booking_system.dto.request.booking.CreateBookingRequest;
import com.example.ticket_booking_system.entity.*;
import com.example.ticket_booking_system.exception.AppException;
import com.example.ticket_booking_system.exception.ErrorCode;
import com.example.ticket_booking_system.mapper.BookingMapper;
import com.example.ticket_booking_system.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    // Inject tất cả Repository cần thiết
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ShowtimeRepository showtimeRepository;
    private final SeatRepository seatRepository;
    private final ComboRepository comboRepository;
    private final PriceService priceService; // Dùng để tính giá ghế

    @Transactional // Rất quan trọng: Đảm bảo tất cả hoặc không gì cả
    public BookingResponse createBooking(CreateBookingRequest request) {

        // 1. Tìm các Entity chính
        User user = userRepository.findById(request.getUserID())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Showtime showtime = showtimeRepository.findById(request.getShowtimeID())
                .orElseThrow(() -> new AppException(ErrorCode.SHOWTIME_NOT_FOUND));

        // 2. Tạo Hóa đơn tổng (Booking)
        Booking booking = Booking.builder()
                .user(user)
                .showtime(showtime)
                .bookingDate(LocalDate.now())
                .status(BookingStatus.CONFIRMED) // Giả sử đã thanh toán
                .build();

        float totalSeatPrice = 0;
        List<BookingDetail> seatDetails = new ArrayList<>();

        // 3. Xử lý Ghế (BookingDetail)
        for (Long seatID : request.getSeatIDs()) {
            Seat seat = seatRepository.findById(seatID)
                    .orElseThrow(() -> new AppException(ErrorCode.SEAT_NOT_FOUND));

            // 3.1. Kiểm tra ghế đã bị đặt chưa
            if (seat.getStatus() != SeatStatus.EMPTY) {
                throw new AppException(ErrorCode.SEAT_ALREADY_BOOKED);
            }

            // 3.2. Lấy giá ghế (đã tính khuyến mãi)
            Float seatPrice = priceService.calculateFinalPrice(seat.getSeatType());
            totalSeatPrice += seatPrice;

            // 3.3. Tạo chi tiết ghế
            BookingDetail detail = BookingDetail.builder()
                    .booking(booking) // Liên kết về hóa đơn tổng
                    .seat(seat)
                    .price(seatPrice) // giá ghế
                    .build();
            seatDetails.add(detail);

            // 3.4. (highRisk) Chuyển trạng thái ghế
            seat.setStatus(SeatStatus.SOLD);
            seatRepository.save(seat);
        }
        booking.setBookingDetails(seatDetails); // Gán chi tiết ghế vào Hóa đơn

        // 4. Xử lý Combo (BookingComboDetail)
        float totalComboPrice = 0;
        List<BookingComboDetail> comboDetails = new ArrayList<>();

        if (request.getCombos() != null) {
            for (BookingComboRequest comboReq : request.getCombos()) {
                Combo combo = comboRepository.findById(comboReq.getComboID())
                        .orElseThrow(() -> new AppException(ErrorCode.COMBO_NOT_FOUND));

                // 4.1. giá combo
                Float comboPrice = combo.getUnitPrice();
                totalComboPrice += (comboPrice * comboReq.getQuantity());

                // 4.2. Tạo chi tiết combo
                BookingComboDetail detail = BookingComboDetail.builder()
                        .booking(booking) // Liên kết về hóa đơn tổng
                        .combo(combo)
                        .quantity(comboReq.getQuantity())
                        .unitPrice(comboPrice) // giá 1 combo
                        .build();
                comboDetails.add(detail);
            }
        }
        booking.setBookingComboDetails(comboDetails); // Gán ds chi tiết combo vào Hóa đơn

        // 5. Tính tổng tiền cuối cùng và Lưu
        booking.setTotalPrice(totalSeatPrice + totalComboPrice);
        Booking savedBooking = bookingRepository.save(booking); // Lưu hóa đơn (tự lưu các Detail)

        // 6. Map sang Response DTO để trả về
        return BookingMapper.toBookingResponse(savedBooking);
    }
}
