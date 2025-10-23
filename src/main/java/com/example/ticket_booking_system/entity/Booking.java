package com.example.ticket_booking_system.entity;

import com.example.ticket_booking_system.Enum.BookingStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "tblBooking", schema = "dbo")
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "showtimeID", nullable = false)
    private Showtime showtime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userID", nullable = false)
    private User user;

    @Column(name = "bookingDate", nullable = false)
    private LocalDate bookingDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BookingStatus status; // (PENDING, CONFIRMED, CANCELLED)

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BookingDetail> bookingDetails;


}
