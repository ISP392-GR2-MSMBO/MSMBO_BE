package com.example.ticket_booking_system.service;

import com.example.ticket_booking_system.Enum.SeatStatus;
import com.example.ticket_booking_system.dto.request.seat.CreateSingleSeatRequest;
import com.example.ticket_booking_system.entity.Seat;
import com.example.ticket_booking_system.entity.SeatType;
import com.example.ticket_booking_system.exception.AppException;
import com.example.ticket_booking_system.exception.ErrorCode;
import com.example.ticket_booking_system.repository.SeatRepository;
import com.example.ticket_booking_system.repository.SeatTypeRepository;
import com.example.ticket_booking_system.repository.TheaterRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor

public class SeatService {
    private final SeatRepository seatRepository;
    private final PriceService priceService;
    private final TheaterRepository theaterRepository;
    private final SeatTypeRepository seatTypeRepository;

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

        SeatStatus currentStatus = seat.getStatus();

        if(currentStatus == newStatus){
            return seat;
        }

        // Cập nhật trạng thái ghế
        seat.setStatus(newStatus);

        // Lưu lại vào DB
        return seatRepository.save(seat);
    }

    @Transactional
    public List<Seat> updateMultipleSeatsStatus(List<Long> seatIds, SeatStatus newStatus){

        // 1. Check if the target status is valid (should only be AVAILABLE or UNAVAILABLE)
        if (newStatus != SeatStatus.AVAILABLE && newStatus != SeatStatus.UNAVAILABLE) {
            // You might need to create this ErrorCode
            throw new AppException(ErrorCode.INVALID_SEAT_STATUS_TRANSITION);
        }

        // 2. Fetch all seats at once
        List<Seat> seats = seatRepository.findAllById(seatIds);

        // 3. Verify all seats were found
        if (seats.size() != seatIds.size()) {
            // Consider providing more detail about which IDs weren't found if possible
            throw new AppException(ErrorCode.SEAT_NOT_FOUND);
        }

        // 4. Iterate and update status in memory
        for (Seat seat : seats) {
            // Only update if the status is actually different
            if (seat.getStatus() != newStatus) {
                // No complex business logic needed here anymore,
                // as we only allow AVAILABLE <-> UNAVAILABLE transitions.
                seat.setStatus(newStatus);
            }
            // If seat.getStatus() == newStatus, we do nothing and continue
        }

        // 5. Save all changes at once
        return seatRepository.saveAll(seats);
    }

    public Seat getSeatById(Long seatId) {
        return seatRepository.findById(seatId)
                .orElseThrow(() -> new AppException(ErrorCode.SEAT_NOT_FOUND));
    }

    public List<Seat> getAllSeat(){
        return seatRepository.findAll();
    }

    public List<SeatType> getAllSeatType(){return seatTypeRepository.findAll();}

    @Transactional
    public void addSeats(Long theaterId, List<CreateSingleSeatRequest> seatRequest){

        if (!theaterRepository.existsById(theaterId)) {
            throw new AppException(ErrorCode.THEATER_NOT_FOUND);
        }

        for(CreateSingleSeatRequest seatReq : seatRequest){
            SeatType seatType = seatTypeRepository.findById(seatReq.getSeatTypeId()).
            orElseThrow(() -> new AppException(ErrorCode.SEAT_TYPE_NOT_FOUND));

            if(seatRepository.existsByTheaterIdAndRowAndNumber(theaterId, seatReq.getRowName(), seatReq.getNumber())){
                throw new AppException(ErrorCode.SEAT_ALREADY_EXISTS);
            }

            Seat newSeat = new Seat();
            newSeat.setTheaterId(theaterId);
            newSeat.setRow(seatReq.getRowName());
            newSeat.setNumber(seatReq.getNumber());
            newSeat.setSeatType(seatType);
            newSeat.setStatus(SeatStatus.UNAVAILABLE);

            seatRepository.save(newSeat);
        }
    }

}
