package com.example.ticket_booking_system.dto.reponse.customer;

import lombok.*;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter

public class CustomerResponse {
    private Long customerID;
    private Long userID;
    private String fullName;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private String gender;
    private String address;
}
