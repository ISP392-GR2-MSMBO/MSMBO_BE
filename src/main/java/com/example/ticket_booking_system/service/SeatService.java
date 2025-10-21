package com.example.ticket_booking_system.service;

import com.example.ticket_booking_system.Enum.SeatStatus;
import com.example.ticket_booking_system.entity.Seat;
import com.example.ticket_booking_system.exception.AppException;
import com.example.ticket_booking_system.exception.ErrorCode;
import com.example.ticket_booking_system.repository.SeatRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor

public class SeatService {
    private final SeatRepository seatRepository;
    private final PriceService priceService;

    public List<Seat> getSeatsByTheater(Long theaterId) {
        List<Seat> seats = seatRepository.findByTheaterId(theaterId);
//        for (Seat s: seats){
//            if(s.isBroken()){
//                s.setStatus(SeatStatus.UNAVAILABLE);
//            }
//        }
        return seats;
    }

    public Seat updateSeatStatus(Long seatId, SeatStatus newStatus) {
        // Tìm ghế theo ID, nếu không thấy thì ném lỗi
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new AppException(ErrorCode.SEAT_NOT_FOUND));

        // Nếu ghế bị hỏng thì không cho cập nhật trạng thái thành EMPTY hoặc SOLD
//        if (seat.isBroken() && (newStatus == SeatStatus.EMPTY || newStatus == SeatStatus.SOLD)) {
//            throw new AppException(ErrorCode.SEAT_UNAVAILABLE_DUE_TO_DAMAGE);
//        }

        if (seat.getStatus() == SeatStatus.UNAVAILABLE && newStatus == SeatStatus.SOLD) {
            throw new AppException(ErrorCode.SEAT_UNAVAILABLE_DUE_TO_DAMAGE);
        }

        // Nếu ghế đã bán thì không cho chuyển về EMPTY
        if (seat.getStatus() == SeatStatus.SOLD && newStatus == SeatStatus.EMPTY) {
            throw new AppException(ErrorCode.INVALID_SEAT_STATUS_TRANSITION);
        }

        // Cập nhật trạng thái ghế
        seat.setStatus(newStatus);

        // Lưu lại vào DB
        return seatRepository.save(seat);
    }

    public Seat getSeatById(Long seatId) {
        return seatRepository.findById(seatId)
                .orElseThrow(() -> new AppException(ErrorCode.SEAT_NOT_FOUND));
    }

    public List<Seat> getAllSeat(){
        return seatRepository.findAll();
    }

}
