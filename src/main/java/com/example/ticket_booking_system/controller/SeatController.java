package com.example.ticket_booking_system.controller;

import com.example.ticket_booking_system.Enum.SeatStatus;
import com.example.ticket_booking_system.dto.reponse.seat.SeatResponse;
import com.example.ticket_booking_system.dto.request.seat.SeatRequest;
import com.example.ticket_booking_system.entity.Seat;
import com.example.ticket_booking_system.mapper.SeatMapper;
import com.example.ticket_booking_system.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seat")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000") // cần thêm khi nối React
public class SeatController {


    private final SeatService seatService;

    @GetMapping("/theater/{theaterId}")
    public ResponseEntity<List<SeatResponse>> viewSeats(@PathVariable Long theaterId){
        List<Seat> seats = seatService.getSeatsByTheater(theaterId);
        List<SeatResponse> responses = seats.stream().map(SeatMapper::toResponse).toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/all")
    public ResponseEntity<List<SeatResponse>> viewAllSeats(){
        List<Seat> seats = seatService.getAllSeat();
        List<SeatResponse> responses = seats.stream().map(SeatMapper::toResponse).toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{seatID}")
    public ResponseEntity<SeatResponse> getSeatById(@PathVariable("seatID") Long seatId) {
        Seat seat = seatService.getSeatById(seatId);
        return ResponseEntity.ok(SeatMapper.toResponse(seat));
    }

    @PutMapping
    public ResponseEntity<SeatResponse> updateSeatStatus(@RequestBody SeatRequest request){
        SeatStatus status = SeatStatus.valueOf(request.getStatus().name().toUpperCase());
        Seat seat = seatService.updateSeatStatus(request.getSeatID(), status);
        return ResponseEntity.ok(SeatMapper.toResponse(seat));
    }

}
