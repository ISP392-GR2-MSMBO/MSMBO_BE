package com.example.ticket_booking_system.entity;

import com.example.ticket_booking_system.Enum.BookingDetailStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "tblBookingDetail")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingDetailID;

    // Liên kết về Hóa đơn tổng
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookingID", nullable = false)
    private Booking booking;

    // Ghế nào được đặt
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seatID", nullable = false)
    private Seat seat;

    // Giá của 1 ghế này (snapshot)
    @Column(nullable = false)
    private Float price;

    // --- THUỘC TÍNH MỚI ---
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private BookingDetailStatus status = BookingDetailStatus.ACTIVE;
}