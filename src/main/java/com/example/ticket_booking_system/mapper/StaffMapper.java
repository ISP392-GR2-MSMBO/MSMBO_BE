package com.example.ticket_booking_system.mapper;

import com.example.ticket_booking_system.dto.reponse.staff.StaffResponse;
import com.example.ticket_booking_system.dto.request.staff.StaffRequest;
import com.example.ticket_booking_system.entity.Staff;
import com.example.ticket_booking_system.entity.User;

public class StaffMapper {

    // Convert request → entity
    public static Staff toEntity(StaffRequest request) {
        if (request == null) return null;

        Staff staff = new Staff();
        staff.setDepartment(request.getDepartment());
        staff.setPosition(request.getPosition());
        staff.setSalary(request.getSalary());
        return staff;
    }

    // Convert entity → response
    public static StaffResponse toResponse(Staff staff) {
        if (staff == null) return null;

        StaffResponse response = new StaffResponse();
        response.setStaffID(staff.getStaffID());
        response.setDepartment(staff.getDepartment());
        response.setPosition(staff.getPosition());
        response.setSalary(staff.getSalary());

        // Lấy thông tin User
        User user = staff.getUser();
        if (user != null) {
            response.setUserID(user.getUserID());
            response.setFullName(user.getFullName());
            response.setEmail(user.getEmail());
            response.setPhone(user.getPhone());
        }

        return response;
    }
}
