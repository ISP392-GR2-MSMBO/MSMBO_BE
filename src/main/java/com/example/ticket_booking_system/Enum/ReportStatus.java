package com.example.ticket_booking_system.Enum;

public enum ReportStatus {
    PENDING,      // Mới gửi, đang chờ
    IN_PROGRESS,  // Đã tiếp nhận và đang xử lý
    RESOLVED,     // Đã giải quyết xong
    REJECTED,      // Từ chối (ví dụ: feedback không hợp lệ)
    ARCHIVED // Đây là "Soft Delete" (Đã ẩn đi)
}