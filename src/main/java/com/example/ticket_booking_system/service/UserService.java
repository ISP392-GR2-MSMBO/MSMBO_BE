package com.example.ticket_booking_system.service;

import com.example.ticket_booking_system.dto.request.user.UserRequest;
import com.example.ticket_booking_system.dto.reponse.user.UserResponse;
import com.example.ticket_booking_system.dto.request.user.UserUpdateProfileRequest;
import com.example.ticket_booking_system.entity.User;
import com.example.ticket_booking_system.exception.AppException;
import com.example.ticket_booking_system.exception.ErrorCode;
import com.example.ticket_booking_system.mapper.UserMapper;
import com.example.ticket_booking_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // Lấy danh sách tất cả user
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toResponse)
                .collect(Collectors.toList());
    }

    // Tạo user mới
    public UserResponse createUser(UserRequest request) {
        if (userRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }
        if (userRepository.existsByPhoneIgnoreCase(request.getPhone())) {
            throw new AppException(ErrorCode.PHONE_EXISTED);
        }
        User user = UserMapper.toEntity(request);
        userRepository.save(user);
        return UserMapper.toResponse(user);
    }

    // 🔍 Tìm người dùng theo username
    public List<UserResponse> searchUsersByUsername(String keyword) {
        List<UserResponse> users = userRepository.findByUserNameContainingIgnoreCase(keyword)
                .stream()
                .map(UserMapper::toResponse)
                .collect(Collectors.toList());

        if (users.isEmpty()) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        return users;
    }

    // 🔍 Tìm người dùng theo full name
    public List<UserResponse> searchUsersByFullName(String keyword) {
        List<UserResponse> users = userRepository.findByFullNameContainingIgnoreCase(keyword)
                .stream()
                .map(UserMapper::toResponse)
                .collect(Collectors.toList());

        if (users.isEmpty()) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        return users;
    }


    // Cập nhật user
    public UserResponse updateUser(Long id, UserUpdateProfileRequest request) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (userRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }
        if (userRepository.existsByPhoneIgnoreCase(request.getPhone())) {
            throw new AppException(ErrorCode.PHONE_EXISTED);
        }
        if (request.getFullName() != null) existingUser.setFullName(request.getFullName());
        if (request.getEmail() != null) existingUser.setEmail(request.getEmail());
        if (request.getPhone() != null) existingUser.setPhone(request.getPhone());

        userRepository.save(existingUser);
        return UserMapper.toResponse(existingUser);
    }

    // Xóa user
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)){
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        userRepository.deleteById(id);
    }
}
