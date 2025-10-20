package com.example.ticket_booking_system.mapper;


import com.example.ticket_booking_system.dto.reponse.seat.SeatResponse;
import com.example.ticket_booking_system.dto.request.seat.SeatRequest;
import com.example.ticket_booking_system.entity.Seat;
import com.example.ticket_booking_system.entity.SeatType;

public class SeatMapper {
    public static Seat toEntity(SeatRequest request) {
        Seat seat = new Seat();
        seat.setSeatID(request.getSeatID());
        seat.setTheaterId(request.getTheaterId());
        seat.setRow(request.getRow());
        seat.setNumber(request.getNumber());
//      seat.set(request.getPrice());
        seat.setStatus(request.getStatus());
        return seat;
    }

    public static SeatResponse toResponse(Seat seat, Float finalPrice, String promotionName){
        SeatResponse res = new SeatResponse();
        res.setSeatID(seat.getSeatID());
        res.setTheaterId(seat.getTheaterId());
        res.setRow(seat.getRow());
        res.setNumber(seat.getNumber());

        res.setType(seat.getSeatType().getName());
        res.setBasePrice(seat.getSeatType().getBasePrice());

        res.setFinalePrice(finalPrice);
        res.setPromotionName(promotionName);

        res.setStatus(seat.getStatus().name());
        return res;
    }

    public static SeatResponse toResponse(Seat seat, Float finalPrice) {
        return toResponse(seat, finalPrice, null);
    }

    public static SeatResponse toResponse(Seat seat) {
        SeatResponse res = new SeatResponse();
        res.setSeatID(seat.getSeatID());
        if (seat.getTheaterId() != null) {
            res.setTheaterId(seat.getTheaterId());
        }
        res.setRow(seat.getRow());
        res.setNumber(seat.getNumber());

        SeatType seatType = seat.getSeatType();
        if (seatType != null) {
            res.setType(seatType.getName());
            res.setBasePrice(seatType.getBasePrice());
            // Gán giá cuối = giá gốc vì không tính toán KM
            res.setFinalePrice(seatType.getBasePrice());
        }

        res.setStatus(seat.getStatus().name());
        return res;
    }
}
