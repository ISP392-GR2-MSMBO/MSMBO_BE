package com.example.ticket_booking_system.mapper;

import com.example.ticket_booking_system.dto.reponse.report.ReportResponse;
import com.example.ticket_booking_system.entity.Report;
import com.example.ticket_booking_system.entity.User;

public class ReportMapper {

    /**
     * Chuyển từ Entity Report sang ReportResponse DTO
     * (Phiên bản rút gọn chỉ trả về senderUserID)
     */
    public static ReportResponse toReportResponse(Report report) {

        return ReportResponse.builder()
                .reportID(report.getReportID())
                .description(report.getDescription())
                .status(report.getStatus())
                .reportType(report.getReportType())
                .createdDate(report.getCreatedDate())
                .senderUserID(report.getSenderUser().getUserID())
                .build();
    }
}