package com.example.ticket_booking_system.service;

import com.example.ticket_booking_system.Enum.BookingDetailStatus;
import com.example.ticket_booking_system.Enum.BookingStatus;
import com.example.ticket_booking_system.Enum.SeatStatus;
import com.example.ticket_booking_system.dto.reponse.booking.BookingDetailResponse;
import com.example.ticket_booking_system.dto.reponse.booking.BookingResponse;
import com.example.ticket_booking_system.dto.reponse.seat.SeatResponse;
import com.example.ticket_booking_system.dto.request.booking.CreateBookingRequest;
import com.example.ticket_booking_system.entity.*;
import com.example.ticket_booking_system.exception.AppException;
import com.example.ticket_booking_system.exception.ErrorCode;
import com.example.ticket_booking_system.mapper.BookingMapper;
import com.example.ticket_booking_system.mapper.SeatMapper;
import com.example.ticket_booking_system.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.ticket_booking_system.repository.PaymentRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {

    // --- Inject tất cả Repository và Service cần thiết ---
    private final BookingRepository bookingRepository;
    private final BookingDetailRepository bookingDetailRepository;
    private final UserRepository userRepository;
    private final ShowtimeRepository showtimeRepository;
    private final SeatRepository seatRepository;
    private final PaymentRepository paymentRepository;
    private final PriceService priceService;// Dùng để tính giá ghế

    /**
     * CHỨC NĂNG 1: TẠO BOOKING MỚI (ĐẶT VÉ)
     */
    @Transactional // Rất quan trọng: Đảm bảo tất cả hoặc không gì cả
    public BookingResponse createBooking(CreateBookingRequest request) {

        // 1. Tìm các Entity chính
        User user = userRepository.findById(request.getUserID())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Showtime showtime = showtimeRepository.findById(request.getShowtimeID())
                .orElseThrow(() -> new AppException(ErrorCode.SHOWTIME_NOT_FOUND));

        // 2. Lấy danh sách ghế đã bán CHO SUẤT CHIẾU NÀY (Logic an toàn)
        List<BookingDetail> soldDetails = bookingDetailRepository.findAllByBooking_Showtime_ShowtimeID(request.getShowtimeID());
        Set<Long> soldSeatIds = soldDetails.stream()
                .filter(detail -> detail.getStatus() == BookingDetailStatus.ACTIVE) //
                .map(detail -> detail.getSeat().getSeatID())
                .collect(Collectors.toSet());

        // 3. Tạo Hóa đơn tổng (Booking)
        Booking booking = Booking.builder()
                .user(user)
                .showtime(showtime)
                .bookingDate(LocalDate.now())
                .status(BookingStatus.PENDING)
                .build();

        float totalSeatPrice = 0;
        List<BookingDetail> seatDetailsList = new ArrayList<>();

        // 4. Xử lý Ghế (BookingDetail)
        for (Long seatID : request.getSeatIDs()) {
            Seat seat = seatRepository.findById(seatID)
                    .orElseThrow(() -> new AppException(ErrorCode.SEAT_NOT_FOUND));

            // 4.1. Kiểm tra ghế có bị admin khóa (hỏng) không
            if (seat.getStatus() == SeatStatus.UNAVAILABLE) {
                throw new AppException(ErrorCode.SEAT_UNAVAILABLE_DUE_TO_DAMAGE);
            }

            // 4.2. Kiểm tra ghế đã bị bán (cho suất chiếu này) chưa
            if (soldSeatIds.contains(seatID)) {
                throw new AppException(ErrorCode.SEAT_ALREADY_BOOKED);
            }

            // 4.3. Lấy giá ghế (đã tính khuyến mãi)
            Float seatPrice = priceService.calculateFinalPrice(seat.getSeatType());
            totalSeatPrice += seatPrice;

            // 4.4. Tạo chi tiết ghế
            BookingDetail detail = BookingDetail.builder()
                    .booking(booking) // Liên kết về hóa đơn tổng
                    .seat(seat)
                    .price(seatPrice) // "Snapshot" giá ghế
                    .status(BookingDetailStatus.ACTIVE)
                    .build();
            seatDetailsList.add(detail);
        }
        booking.setBookingDetails(seatDetailsList); // Gán ds chi tiết ghế vào Hóa đơn

        // 5. Tính tổng tiền cuối cùng và Lưu
        booking.setTotalPrice(totalSeatPrice);
        Booking savedBooking = bookingRepository.save(booking); // Lưu hóa đơn (sẽ tự lưu các Detail)

        // 6. Map sang Response DTO để trả về
        return BookingMapper.toBookingResponse(savedBooking);
    }

    /**
     * CHỨC NĂNG 2: LẤY SƠ ĐỒ GHẾ (CHO 1 SUẤT CHIẾU) - Trả về TẤT CẢ ghế
     */
    public List<SeatResponse> getSeatMapForShowtime(Long showtimeId) {

        // 1. Tìm showtime để lấy theaterId
        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new AppException(ErrorCode.SHOWTIME_NOT_FOUND));
        Long theaterId = showtime.getTheater().getTheaterID();

        // 2. Lấy TẤT CẢ ghế có trong rạp (SƠ ĐỒ GỐC)
        List<Seat> allSeatsInTheater = seatRepository.findByTheaterId(theaterId);

        // 3. Lấy TẤT CẢ ghế ĐÃ BÁN (SOLD) cho suất chiếu này
        List<BookingDetail> allDetails = bookingDetailRepository.findAllByBooking_Showtime_ShowtimeID(showtimeId);
        // 3a. Lọc ra những ghế đã BÁN (Booking = CONFIRMED)
        Set<Long> soldSeatIds = allDetails.stream()
                .filter(detail -> detail.getBooking().getStatus() == BookingStatus.CONFIRMED &&
                        detail.getStatus() == BookingDetailStatus.ACTIVE)
                .map(detail -> detail.getSeat().getSeatID())
                .collect(Collectors.toSet());

        // 3b. Lọc ra những ghế đang CHỜ (Booking = PENDING)
        Set<Long> pendingSeatIds = allDetails.stream()
                .filter(detail -> detail.getBooking().getStatus() == BookingStatus.PENDING &&
                        detail.getStatus() == BookingDetailStatus.ACTIVE)
                .map(detail -> detail.getSeat().getSeatID())
                .collect(Collectors.toSet());

        // 4. Duyệt qua SƠ ĐỒ GỐC (allSeatsInTheater)
        return allSeatsInTheater.stream().map(seat -> {

            // Tính giá (đã có KM)
            Float finalPrice = priceService.calculateFinalPrice(seat.getSeatType());
            // Map sang DTO
            SeatResponse response = SeatMapper.toResponse(seat, finalPrice);

            // 5. Gán trạng thái (Status)
            // Ưu tiên 1: Ghế bị admin khóa (hỏng)
            if (seat.getStatus() == SeatStatus.UNAVAILABLE) {
                response.setStatus(SeatStatus.UNAVAILABLE.name());
            }
            // Ưu tiên 2: Ghế đã bán (cho suất này)
            else if (soldSeatIds.contains(seat.getSeatID())) {
                response.setStatus("SOLD"); // Trạng thái "SOLD" này là động
            }
            // Mặc định: Ghế còn trống
            else {
                response.setStatus(SeatStatus.AVAILABLE.name());
            }

            return response;
        }).collect(Collectors.toList());
    }

    /**
     * CHỨC NĂNG 3: LẤY DANH SÁCH CHỈ NHỮNG GHẾ ĐÃ BÁN (HÀM MỚI)
     */
    public List<SeatResponse> getSoldSeatsForShowtime(Long showtimeId) {

        // 1. Kiểm tra xem showtime có tồn tại không
        if (!showtimeRepository.existsById(showtimeId)) {
            throw new AppException(ErrorCode.SHOWTIME_NOT_FOUND);
        }

        // 2. Lấy TẤT CẢ ghế ĐÃ BÁN (SOLD) cho suất chiếu này
        List<BookingDetail> soldDetails = bookingDetailRepository.findAllByBooking_Showtime_ShowtimeID(showtimeId);

        // 3. Chuyển danh sách chi tiết (BookingDetail) sang danh sách ghế (SeatResponse)
        return soldDetails.stream()
                .filter(detail -> detail.getStatus() == BookingDetailStatus.ACTIVE)
                .map(detail -> {
                    Seat seat = detail.getSeat();

                    // Lấy giá đã "snapshot" lúc bán
                    Float priceAtBooking = detail.getPrice();

                    // Map sang DTO (Dùng mapper 2 tham số)
                    SeatResponse response = SeatMapper.toResponse(seat, priceAtBooking);

                    // Gán trạng thái
                    response.setStatus("SOLD");

                    return response;
                })
                .collect(Collectors.toList());
    }

    /**
     * CHỨC NĂNG 4: LẤY LỊCH SỬ ĐẶT VÉ CỦA USER
     */
    public List<BookingResponse> getBookingsByUserId(Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        // Gọi hàm repository
        List<Booking> bookings = bookingRepository.findByUserUserIDOrderByBookingDateDesc(userId);

        // Map kết quả sang DTO
        return bookings.stream()
                .map(BookingMapper::toBookingResponse)
                .collect(Collectors.toList());
    }

    /**
     * CHỨC NĂNG 5: LẤY CHI TIẾT 1 VÉ (BOOKING) CỤ THỂ
     */
    public BookingResponse getBookingById(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_FOUND));

        return BookingMapper.toBookingResponse(booking);
    }

    /**
     * CHỨC NĂNG 6: LẤY CHI TIẾT 1 GHẾ (BookingDetail) CỤ THỂ
     */
    public BookingDetailResponse getBookingDetailById(Long bookingDetailId) {

        BookingDetail detail = bookingDetailRepository.findById(bookingDetailId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_DETAIL_NOT_FOUND));

        return BookingMapper.toBookingDetailResponse(detail);
    }

    /**
     * CHỨC NĂNG 7: Xóa cứng các hóa đơn do khách hàng cancelled và giải phóng ghế
     */
    @Transactional
    public void hardDeleteBooking(Long bookingId) {
        // 1. Tìm booking, nếu không thấy thì báo lỗi
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_FOUND));

        // 2. Chỉ xóa nếu booking đang ở trạng thái PENDING
        if (booking.getStatus() != BookingStatus.PENDING) {
            // Bạn có thể tạo ErrorCode mới: CANNOT_DELETE_CONFIRMED_BOOKING
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }

        // 3. Xóa Payment liên quan trước (để tránh lỗi khóa ngoại)
        // Chúng ta phải dùng @Transactional để đảm bảo cả 2 cùng thành công
        paymentRepository.deleteByBooking_BookingID(bookingId);

        // 4. Xóa Booking (sẽ tự động cascade xóa BookingDetail)
        bookingRepository.deleteById(bookingId);
    }
}