package com.example.ticket_booking_system.controller;

import com.example.ticket_booking_system.Enum.SeatStatus;
import com.example.ticket_booking_system.dto.reponse.seat.SeatResponse;
import com.example.ticket_booking_system.dto.request.seat.AddSeatRequest;
import com.example.ticket_booking_system.dto.request.seat.SeatRequest;
import com.example.ticket_booking_system.dto.request.seat.UpdateMultipleSeatRequest;
import com.example.ticket_booking_system.entity.Seat;
import com.example.ticket_booking_system.entity.SeatType;
import com.example.ticket_booking_system.mapper.SeatMapper;
import com.example.ticket_booking_system.service.PriceService;
import com.example.ticket_booking_system.service.SeatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seat")
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:3000")
@CrossOrigin(origins = "https://chillcinema.netlify.app/") // cần thêm khi nối React cần thêm khi nối React
public class SeatController {


    private final SeatService seatService;
    private final PriceService priceService;

    @GetMapping("/theater/{theaterId}")
    public ResponseEntity<List<SeatResponse>> viewSeats(@PathVariable Long theaterId){
        List<Seat> seats = seatService.getSeatsByTheater(theaterId);

        // SỬA LẠI ĐOẠN NÀY
        List<SeatResponse> responses = seats.stream().map(seat -> {
            // 1. Tính giá cuối cùng cho từng ghế
            Float finalPrice = priceService.calculateFinalPrice(seat.getSeatType());

            // 2. Gọi mapper với 2 tham số
            return SeatMapper.toResponse(seat, finalPrice);

        }).toList(); // KẾT THÚC SỬA

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/all")
    public ResponseEntity<List<SeatResponse>> viewAllSeats(){
        List<Seat> seats = seatService.getAllSeat();
        List<SeatResponse> responses = seats.stream().map(SeatMapper::toResponse).toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/seat-type/all")
    public ResponseEntity<List<SeatType>> viewAllSeatTypes(){
        List<SeatType> seatTypes = seatService.getAllSeatType();
        return ResponseEntity.ok(seatTypes);
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

    @PutMapping("/seats/status")
    public ResponseEntity<List<SeatResponse>> updateMultipleSeatStatus( @Valid @RequestBody UpdateMultipleSeatRequest request){

        List<Seat> updateSeats = seatService.updateMultipleSeatsStatus(request.getSeatIDs(), request.getNewStatus());
        List<SeatResponse> responses = updateSeats.stream().map(SeatMapper::toResponse).toList();
        return ResponseEntity.ok(responses);
    }

    @PostMapping("theater/{theaterId}/add-seats")
    public ResponseEntity<Void> addSeatsToTheater(
            @PathVariable Long theaterId,
            @Valid @RequestBody AddSeatRequest request
    ) {
        seatService.addSeats(theaterId, request.getSeats()); // Gửi list DTO con vào Service
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
