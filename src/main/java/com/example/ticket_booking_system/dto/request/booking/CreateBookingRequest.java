package com.example.ticket_booking_system.dto.request.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CreateBookingRequest {

    @NotNull(message = "Showtime ID cannot be null")
    private Long showtimeID;

    // (Lưu ý: UserID sẽ được lấy từ Token, không cần gửi lên)
    @NotNull(message = "User ID cannot be null")
     private Long userID;

    @NotEmpty(message = "You must select at least one seat")
    private List<Long> seatIDs;

    @Valid // Đảm bảo các phần tử bên trong List validate
    private List<BookingComboRequest> combos; // Có thể là list rỗng
}
