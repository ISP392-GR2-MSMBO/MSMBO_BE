package com.example.ticket_booking_system.service;

import com.example.ticket_booking_system.dto.request.user.UserRequest;
import com.example.ticket_booking_system.dto.reponse.user.UserResponse;
import com.example.ticket_booking_system.entity.User;
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
            throw new RuntimeException("Email already exists");
        }
        if (userRepository.existsByPhoneIgnoreCase(request.getPhone())) {
            throw new RuntimeException("Phone already exists");
        }
        User user = UserMapper.toEntity(request);
        userRepository.save(user);
        return UserMapper.toResponse(user);
    }

    // Tìm user theo username
    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return UserMapper.toResponse(user);
    }

    //tim nguoi dung dua theo full name
    public List<UserResponse> searchUsersByFullName(String keyword) {
        return userRepository.findByFullNameContainingIgnoreCase(keyword)
                .stream()
                .map(UserMapper::toResponse)
                .collect(Collectors.toList());
    }

    // Cập nhật user
    public UserResponse updateUser(Long id, UserRequest request) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        existingUser.setFullName(request.getFullName());
        existingUser.setEmail(request.getEmail());
        existingUser.setPhone(request.getPhone());
        existingUser.setRoleID(request.getRole());
        existingUser.setStatus(request.isStatus());

        userRepository.save(existingUser);
        return UserMapper.toResponse(existingUser);
    }

    // Xóa user
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)){
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }
}
