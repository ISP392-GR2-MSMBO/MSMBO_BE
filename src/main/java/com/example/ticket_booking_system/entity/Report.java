package com.example.ticket_booking_system.entity;

import com.example.ticket_booking_system.Enum.ReportStatus;
import com.example.ticket_booking_system.Enum.ReportType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tblReports")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportID;

    // Người gửi báo cáo (Luôn cần biết ai gửi)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "senderUserID", nullable = false)
    private User senderUser;

    @Column(columnDefinition = "NVARCHAR(MAX)", nullable = false)
    private String description; // Nội dung

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ReportStatus status = ReportStatus.PENDING;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReportType reportType;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;
}