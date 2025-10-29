package com.example.ticket_booking_system.dto.request.report;

import com.example.ticket_booking_system.Enum.ReportStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateReportStatusRequest {

    @NotNull(message = "Status cannot be null")
    private ReportStatus status; // PENDING, IN_PROGRESS, RESOLVED, REJECTED, ARCHIVED
}