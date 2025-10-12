package com.example.ticket_booking_system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tblTheaters", schema = "dbo")
public class Theater {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "theaterID")
    private Long theaterID;
    @Column
    private String theaterName;
    @Column
    private String status;
    @OneToMany(mappedBy = "theater", cascade = CascadeType.ALL)
    private List<Showtime> showtime;
}
