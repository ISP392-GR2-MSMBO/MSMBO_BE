package com.example.ticket_booking_system.entity;

import com.example.ticket_booking_system.Enum.Approve;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tblShowtime", schema = "dbo")
public class Showtime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long showtimeID;
    @ManyToOne
    @JoinColumn(name = "theaterID", nullable = false)
    private Theater theater;
    @ManyToOne
    @JoinColumn(name = "movieID", nullable = false)
    private Movie movie;
    private LocalDate date;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    @Schema(example = "10:00")
    private LocalTime startTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    @Schema(example = "12:00")
    private LocalTime endTime;
    @Column(name = "is_deleted", nullable = false)
    @Builder.Default
    private boolean isDeleted = false; // default là chưa xóa
    // ====== Phê duyệt / xuất bản ======
    @Enumerated(EnumType.STRING)
    @Column(name = "approve_status", length = 20, nullable = false)
    @Builder.Default
    private Approve approveStatus = Approve.PENDING;   // mặc định khi tạo mới

    @Column(name = "is_published", nullable = false)
    @Builder.Default
    private boolean isPublished = false;               // chỉ true sau khi admin duyệt
}
