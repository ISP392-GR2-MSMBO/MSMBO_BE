package com.example.ticket_booking_system.dto.request.report;

import com.example.ticket_booking_system.Enum.ReportType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateReportRequest {

    // (Lưu ý: senderUserID sẽ được lấy từ API Path, không cần gửi trong body)

    @NotBlank(message = "Description cannot be blank")
    private String description;

    @NotNull(message = "Report Type cannot be null")
    private ReportType reportType; // CUSTOMER_FEEDBACK, THEATER_ISSUE, SYSTEM_BUG
}