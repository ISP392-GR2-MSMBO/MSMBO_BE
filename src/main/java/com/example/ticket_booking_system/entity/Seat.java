package com.example.ticket_booking_system.entity;
import com.example.ticket_booking_system.Enum.SeatStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "tblSeat", schema = "dbo")
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long seatID;

    @JoinColumn(name = "theaterId")
    @JsonBackReference
    private Long theaterId;

    @Column
    private String row;

    @Column
    private Integer number;

    @Column
    private String type;    //Couple, Vip...

    @Column
    private Float price;

    @Column
    @Enumerated(EnumType.STRING)
    private SeatStatus status;  //Empty, Sold, Unavailable...

    private boolean isBroken = false;
}
