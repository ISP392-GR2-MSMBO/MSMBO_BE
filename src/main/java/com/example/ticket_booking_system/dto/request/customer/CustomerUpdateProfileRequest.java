package com.example.ticket_booking_system.dto.request.customer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter

public class CustomerUpdateProfileRequest {
    @NotBlank(message = "Full name cannot be blank")
    private String fullName;
    @NotBlank(message = "Email cannot be blank")
    private String email;
    @NotBlank(message = "Phone cannot be blank")
    private String phone;
    @NotBlank(message = "Gender cannot be blank")
    private String gender;
    @NotBlank(message = "Address cannot be blank")
    private String address;
    @NotNull(message = "Date of birth cannot be null")
    private LocalDate dateOfBirth;
    private boolean status;
}
