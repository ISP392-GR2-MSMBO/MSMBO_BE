package com.example.ticket_booking_system.controller;

import com.example.ticket_booking_system.Enum.ReportStatus;
import com.example.ticket_booking_system.Enum.ReportType;
import com.example.ticket_booking_system.dto.reponse.report.ReportResponse;
import com.example.ticket_booking_system.dto.request.report.CreateReportRequest;
import com.example.ticket_booking_system.dto.request.report.UpdateReportStatusRequest;
import com.example.ticket_booking_system.service.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ReportController {

    private final ReportService reportService;

    /**
     * API 1: User/Staff/Manager gửi một báo cáo mới
     * (senderId lấy từ path)
     */
    @PostMapping("/sender/{senderId}")
    public ResponseEntity<ReportResponse> createReport(
            @PathVariable Long senderId,
            @Valid @RequestBody CreateReportRequest request) {

        ReportResponse response = reportService.createReport(senderId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * API 2: Staff/Manager/Admin cập nhật trạng thái của 1 báo cáo
     */
    @PatchMapping("/{reportId}/status")
    public ResponseEntity<ReportResponse> updateReportStatus(
            @PathVariable Long reportId,
            @Valid @RequestBody UpdateReportStatusRequest request) {

        ReportResponse response = reportService.updateReportStatus(reportId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * API 3: Lấy báo cáo theo "Hàng đợi" (Queue)
     * (Dùng cho Staff, Manager, Admin)
     * * Ví dụ:
     * GET .../queue?type=CUSTOMER_FEEDBACK (Lấy tất cả feedback của khách)
     * GET .../queue?type=CUSTOMER_FEEDBACK&status=PENDING (Chỉ lấy feedback PENDING)
     * GET .../queue?type=SYSTEM_BUG (Lấy tất cả lỗi hệ thống cho Admin)
     */
    @GetMapping("/queue")
    public ResponseEntity<List<ReportResponse>> getReportsQueue(
            @RequestParam ReportType type,
            @RequestParam(required = false) ReportStatus status) {

        List<ReportResponse> responses = reportService.getReportsQueue(type, status);
        return ResponseEntity.ok(responses);
    }

    /**
     * API 4: Lấy lịch sử các báo cáo do 1 người đã gửi
     */
    @GetMapping("/sender/{senderId}")
    public ResponseEntity<List<ReportResponse>> getReportsBySender(
            @PathVariable Long senderId) {

        List<ReportResponse> responses = reportService.getReportsBySender(senderId);
        return ResponseEntity.ok(responses);
    }
    /**
     * API 5 (MỚI): Lấy chi tiết của 1 báo cáo cụ thể bằng ID
     **/
    @GetMapping("/{reportId}")
    public ResponseEntity<ReportResponse> getReportById(@PathVariable Long reportId) {
        ReportResponse response = reportService.getReportById(reportId);
        return ResponseEntity.ok(response);
    }
}