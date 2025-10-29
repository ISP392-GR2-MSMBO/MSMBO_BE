package com.example.ticket_booking_system.service;

import com.example.ticket_booking_system.Enum.ReportStatus;
import com.example.ticket_booking_system.Enum.ReportType;
import com.example.ticket_booking_system.dto.reponse.report.ReportResponse;
import com.example.ticket_booking_system.dto.request.report.CreateReportRequest;
import com.example.ticket_booking_system.dto.request.report.UpdateReportStatusRequest;
import com.example.ticket_booking_system.entity.Report;
import com.example.ticket_booking_system.entity.User;
import com.example.ticket_booking_system.exception.AppException;
import com.example.ticket_booking_system.exception.ErrorCode;
import com.example.ticket_booking_system.mapper.ReportMapper;
import com.example.ticket_booking_system.repository.ReportRepository;
import com.example.ticket_booking_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

    /**
     * CHỨC NĂNG 1: Tạo một báo cáo mới
     */
    @Transactional
    public ReportResponse createReport(Long senderId, CreateReportRequest request) {
        // 1. Tìm người gửi
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // 2. Tạo đối tượng Report
        Report report = Report.builder()
                .senderUser(sender)
                .description(request.getDescription())
                .reportType(request.getReportType())
                .status(ReportStatus.PENDING) // Mặc định khi mới tạo
                .build();

        // 3. Lưu vào CSDL
        Report savedReport = reportRepository.save(report);

        // 4. Map sang DTO để trả về
        return ReportMapper.toReportResponse(savedReport);
    }

    /**
     * CHỨC NĂNG 2: Cập nhật trạng thái (Dùng cho Staff/Manager/Admin)
     */
    @Transactional
    public ReportResponse updateReportStatus(Long reportId, UpdateReportStatusRequest request) {
        // 1. Tìm báo cáo
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new AppException(ErrorCode.REPORT_NOT_FOUND)); // (Cần thêm ErrorCode này)

        // 2. Cập nhật trạng thái
        report.setStatus(request.getStatus());

        // 3. Lưu lại
        Report updatedReport = reportRepository.save(report);

        // 4. Map sang DTO
        return ReportMapper.toReportResponse(updatedReport);
    }

    /**
     * CHỨC NĂNG 3: Lấy báo cáo theo "Hàng đợi" (Queue)
     * (Lọc theo Loại, và có thể lọc thêm theo Trạng thái)
     */
    public List<ReportResponse> getReportsQueue(ReportType type, ReportStatus status) {
        List<Report> reports;

        if (status != null) {
            // A. Nếu có trạng thái (vd: PENDING) -> Lọc theo cả 2
            reports = reportRepository.findByReportTypeAndStatusOrderByCreatedDateAsc(type, status);
        } else {
            // B. Nếu không có trạng thái -> Lấy tất cả (trừ ARCHIVED)
            reports = reportRepository.findByReportTypeOrderByCreatedDateAsc(type)
                    .stream()
                    .filter(r -> r.getStatus() != ReportStatus.ARCHIVED)
                    .collect(Collectors.toList());
        }

        return reports.stream()
                .map(ReportMapper::toReportResponse)
                .collect(Collectors.toList());
    }

    /**
     * CHỨC NĂNG 4: Lấy các báo cáo do một người gửi (Lịch sử gửi)
     */
    public List<ReportResponse> getReportsBySender(Long senderId) {
        if (!userRepository.existsById(senderId)) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        List<Report> reports = reportRepository.findBySenderUserUserIDOrderByCreatedDateDesc(senderId);

        return reports.stream()
                .map(ReportMapper::toReportResponse)
                .collect(Collectors.toList());
    }
    /**
     * CHỨC NĂNG 5 (MỚI): Lấy chi tiết 1 báo cáo bằng ID
     */
    public ReportResponse getReportById(Long reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new AppException(ErrorCode.REPORT_NOT_FOUND));

        return ReportMapper.toReportResponse(report);
    }
}