package com.example.ticket_booking_system.entity;

import com.example.ticket_booking_system.Enum.Approve;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "tblMovies", schema = "dbo")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long movieID;
    @Column
    private String movieName;
    @Column
    private String genre;
    @Column
    private int duration;
    @Column
    private String age;
    @Column
    private String director;
    @Column
    private String actress;
    @Column
    private LocalDate releaseDate;
    @Column
    private String language;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column
    private String poster;
    @Column
    private String trailer;
    @Column
    private String status;
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
