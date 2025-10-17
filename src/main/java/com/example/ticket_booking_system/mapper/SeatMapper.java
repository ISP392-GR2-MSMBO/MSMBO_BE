package com.example.ticket_booking_system.mapper;


import com.example.ticket_booking_system.dto.reponse.seat.SeatResponse;
import com.example.ticket_booking_system.dto.request.seat.SeatRequest;
import com.example.ticket_booking_system.entity.Seat;

public class SeatMapper {
    public static Seat toEntity(SeatRequest request) {
        Seat seat = new Seat();
        seat.setSeatID(request.getSeatID());
        seat.setTheaterId(request.getTheaterId());
        seat.setRow(request.getRow());
        seat.setNumber(request.getNumber());
        seat.setPrice(request.getPrice());
        seat.setStatus(request.getStatus());
        return seat;
    }

    public static SeatResponse toResponse(Seat seat){
        SeatResponse res = new SeatResponse();
        res.setSeatID(seat.getSeatID());
        res.setTheaterId(seat.getTheaterId());
        res.setRow(seat.getRow());
        res.setNumber(seat.getNumber());
        res.setPrice(seat.getPrice());
        res.setStatus(seat.getStatus().name());
        return res;
    }

}
