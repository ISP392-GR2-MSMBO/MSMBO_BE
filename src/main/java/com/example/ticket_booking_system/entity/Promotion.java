package com.example.ticket_booking_system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor

public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long promotionID;

    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private String discountType;//Giam theo %(10%)"percentage", giam theo so tien(20k)"fixed-amount"

    @Column
    private Float discountValue;
    private boolean isActive;

    @ManyToMany
    @JoinTable(
            name = "tblPromotion_SeatType",
            joinColumns = @JoinColumn(name = "promotionID"),
            inverseJoinColumns = @JoinColumn(name = "seatTypeID")
    )
    private Set<SeatType> applicableSeatTypes;
}
