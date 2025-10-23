package com.example.ticket_booking_system.mapper;

import com.example.ticket_booking_system.dto.reponse.user.UserResponse;
import com.example.ticket_booking_system.dto.request.user.UserRequest;
import com.example.ticket_booking_system.entity.User;

public class UserMapper {

    // Convert từ Request sang Entity
    public static User toEntity(UserRequest request) {
        if (request == null) return null;

        User user = new User();
        user.setUserName(request.getUserName());
        user.setFullName(request.getFullName());
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setRoleID(request.getRoleID());
        user.setStatus(request.isStatus());
        // 🆕 mặc định email chưa xác thực khi tạo mới
        user.setEmailVerified(false);
        user.setEmailVerifyToken(null);
        user.setEmailVerifyTokenExp(null);
        return user;
    }

    // Convert từ Entity sang Response
    public static UserResponse toResponse(User user) {
        if (user == null) return null;

        UserResponse response = new UserResponse();
        response.setUserID(user.getUserID());
        response.setUserName(user.getUserName());
        response.setFullName(user.getFullName());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setRoleID(user.getRoleID());
        response.setStatus(user.isStatus());
        response.setEmailVerified(user.isEmailVerified());
        return response;
    }
}
