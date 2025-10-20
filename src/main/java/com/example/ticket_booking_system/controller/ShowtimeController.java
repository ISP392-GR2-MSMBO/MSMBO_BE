package com.example.ticket_booking_system.controller;

import com.example.ticket_booking_system.dto.reponse.showtime.ShowtimeResponse;
import com.example.ticket_booking_system.dto.request.showtime.ShowtimeRequest;
import com.example.ticket_booking_system.entity.Showtime;
import com.example.ticket_booking_system.exception.AppException;
import com.example.ticket_booking_system.exception.ErrorCode;
import com.example.ticket_booking_system.mapper.ShowtimeMapper;
import com.example.ticket_booking_system.service.ShowtimeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

    @RestController
    @RequestMapping("/api/showtime")
    @CrossOrigin(origins = "http://localhost:3000") // thêm khi nối React
    public class ShowtimeController {

        private final ShowtimeService showtimeService;

        @Autowired
        public ShowtimeController(ShowtimeService showtimeService) {
            this.showtimeService = showtimeService;
        }

        // Lấy tất cả showtime
        @GetMapping
        public List<ShowtimeResponse> getAllShowtimes() {
            List<ShowtimeResponse> result = new ArrayList<>();
            for (Showtime s : showtimeService.getPublicShowtimes()) {
                result.add(ShowtimeMapper.toResponse(s));
            }
            return result;
        }

        @GetMapping("/movie/{movieId}")
        public ResponseEntity<List<ShowtimeResponse>> getPublicByMovie(@PathVariable Long movieId) {
            return ResponseEntity.ok(showtimeService.getPublicShowtimesByMovieId(movieId)
                    .stream().map(ShowtimeMapper::toResponse).toList());
        }

        // Thêm showtime mới
        @PostMapping
        public ShowtimeResponse createShowtime(@Valid @RequestBody ShowtimeRequest request) {
            Showtime toSave = ShowtimeMapper.toEntity(request);
            Showtime saved = showtimeService.saveShowtime(toSave);
            return ShowtimeMapper.toResponse(saved);
        }

        // Cập nhật showtime
        @PutMapping("/{id}")
        public ResponseEntity<ShowtimeResponse> updateShowtime(@PathVariable Long id,@Valid @RequestBody ShowtimeRequest request) {
            Showtime updated = showtimeService.updateShowtime(id, ShowtimeMapper.toEntity(request));
            return ResponseEntity.ok(ShowtimeMapper.toResponse(updated));
        }

        // Xoá showtime
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteShowtime(@PathVariable Long id) {
            showtimeService.deleteShowtime(id);
            return ResponseEntity.noContent().build();
        }
        // ==== Admin endpoints (tối giản) cho Showtime ====
        @PostMapping("/{id}/approve")
        public ShowtimeResponse approveShowtime(@PathVariable Long id) {
            var s = showtimeService.adminApproveOrDeny(id, true); // APPROVE & publish
            return ShowtimeMapper.toResponse(s);
        }

        @PostMapping("/{id}/reject")
        public ShowtimeResponse rejectShowtime(@PathVariable Long id) {
            var s = showtimeService.adminApproveOrDeny(id, false); // DENIED & unpublish
            return ShowtimeMapper.toResponse(s);
        }
    }
