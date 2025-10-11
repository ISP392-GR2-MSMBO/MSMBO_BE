package com.example.ticket_booking_system.dto.request.customer;

import lombok.*;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter

public class CustomerRequest {
    private String gender;
    private String address;
    private LocalDate dateOfBirth;
}
