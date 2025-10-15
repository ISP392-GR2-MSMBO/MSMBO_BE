package com.example.ticket_booking_system.controller;

import com.example.ticket_booking_system.dto.reponse.customer.CustomerResponse;
import com.example.ticket_booking_system.dto.request.customer.CustomerRequest;
import com.example.ticket_booking_system.dto.request.customer.CustomerUpdateProfileRequest;
import com.example.ticket_booking_system.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class CustomerController {
//erorr
    private final CustomerService customerService;

    // Register new customer
    @PostMapping("/register")
    public ResponseEntity<CustomerResponse> register(@RequestBody CustomerRequest request) {
        return ResponseEntity.ok(customerService.createCustomer(request));
    }

    // Get profile
    @GetMapping("/{userId}")
    public ResponseEntity<CustomerResponse> getProfile(@PathVariable Long userId) {
        return ResponseEntity.ok(customerService.getCustomerProfile(userId));
    }

    // Update profile
    @PutMapping("/{userId}")
    public ResponseEntity<CustomerResponse> updateProfile(
            @PathVariable Long userId,
            @RequestBody CustomerUpdateProfileRequest request) {
        return ResponseEntity.ok(customerService.updateProfile(userId, request));
    }

    // Soft delete (set inactive)
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Long userId) {
        customerService.deleteCustomer(userId);
        return ResponseEntity.ok("Customer account inactive successfully");
    }
}
