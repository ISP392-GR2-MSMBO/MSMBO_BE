package com.example.ticket_booking_system.dto.reponse.report;

import com.example.ticket_booking_system.Enum.ReportStatus;
import com.example.ticket_booking_system.Enum.ReportType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ReportResponse {

    private Long reportID;
    private String description;
    private ReportStatus status;
    private ReportType reportType;
    private LocalDateTime createdDate;

    // Thông tin người gửi
    private Long senderUserID;
}