package com.example.ticket_booking_system.entity;

import com.example.ticket_booking_system.Enum.BookingDetailStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "tblBookingDetail", schema = "dbo")
@NoArgsConstructor
@AllArgsConstructor
public class BookingDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingDetailID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookingID", nullable = false)
    private Booking booking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seatID", nullable = false)
    private Seat seat;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BookingDetailStatus status;
}
