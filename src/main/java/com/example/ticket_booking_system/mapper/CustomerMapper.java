package com.example.ticket_booking_system.mapper;

import com.example.ticket_booking_system.dto.reponse.customer.CustomerResponse;
import com.example.ticket_booking_system.dto.request.customer.CustomerRequest;
import com.example.ticket_booking_system.entity.Customer;
import com.example.ticket_booking_system.entity.User;

public class CustomerMapper {

    // Chuyển từ request sang entity
    public static Customer toEntity(CustomerRequest request) {
        if (request == null) return null;

        Customer customer = new Customer();
        customer.setGender(request.getGender());
        customer.setAddress(request.getAddress());
        customer.setDateOfBirth(request.getDateOfBirth());
        return customer;
    }

    // Chuyển từ entity sang response
    public static CustomerResponse toResponse(Customer customer) {
        if (customer == null) return null;

        CustomerResponse response = new CustomerResponse();
        response.setCustomerID(customer.getCustomerID());
        response.setGender(customer.getGender());
        response.setAddress(customer.getAddress());
        response.setDateOfBirth(customer.getDateOfBirth());

        // Gộp thông tin User
        User user = customer.getUser();
        if (user != null) {
            response.setUserID(user.getUserID());
            response.setFullName(user.getFullName());
            response.setEmail(user.getEmail());
            response.setPhone(user.getPhone());
        }

        return response;
    }
}
