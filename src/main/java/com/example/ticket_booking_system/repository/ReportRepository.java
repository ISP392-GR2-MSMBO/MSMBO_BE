package com.example.ticket_booking_system.repository;

import com.example.ticket_booking_system.Enum.ReportStatus;
import com.example.ticket_booking_system.Enum.ReportType;
import com.example.ticket_booking_system.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    /**
     * Tìm theo Loại báo cáo (để Staff/Manager/Admin xem "hàng đợi")
     * Sắp xếp theo ngày tạo cũ nhất trước.
     */
    List<Report> findByReportTypeOrderByCreatedDateAsc(ReportType reportType);

    /**
     * Tìm theo Loại VÀ Trạng thái
     * (Ví dụ: Tìm các 'CUSTOMER_FEEDBACK' mà 'PENDING')
     */
    List<Report> findByReportTypeAndStatusOrderByCreatedDateAsc(ReportType reportType, ReportStatus status);

    /**
     * Tìm tất cả báo cáo do 1 người gửi
     */
    List<Report> findBySenderUserUserIDOrderByCreatedDateDesc(Long userId);
}