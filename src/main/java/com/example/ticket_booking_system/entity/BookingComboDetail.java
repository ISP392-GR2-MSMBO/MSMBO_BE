package com.example.ticket_booking_system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "tblBookingComboDetail") // Bảng chi tiết đồ ăn
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingComboDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingComboDetailID;

    // 1. Thuộc về Hóa đơn nào
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookingID", nullable = false)
    private Booking booking;

    // 2. Combo (sản phẩm) nào
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comboID", nullable = false)
    private Combo combo;

    // 3. Số lượng mua
    @Column(nullable = false)
    private Integer quantity;

    // 4. "Snapshot" giá tại thời điểm mua
    // don gia
    @Column(nullable = false)
    private Float unitPrice; //
}