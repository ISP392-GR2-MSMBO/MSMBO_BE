package com.example.ticket_booking_system.service;

import com.example.ticket_booking_system.Enum.Role;
import com.example.ticket_booking_system.dto.request.user.UserRequest;
import com.example.ticket_booking_system.dto.reponse.user.UserResponse;
import com.example.ticket_booking_system.dto.request.user.UserUpdateProfileRequest;
import com.example.ticket_booking_system.entity.User;
import com.example.ticket_booking_system.exception.AppException;
import com.example.ticket_booking_system.exception.ErrorCode;
import com.example.ticket_booking_system.mapper.UserMapper;
import com.example.ticket_booking_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    @Value("${app.base-url}")
    private String baseUrl; // Spring sẽ inject giá trị từ application.properties
    // Lấy danh sách tất cả user
    public List<UserResponse> getAllUsers() {
        return userRepository.findByIsDeleteFalse()
                .stream()
                .map(UserMapper::toResponse)
                .collect(Collectors.toList());
    }

    // Tạo user mới
    public UserResponse createUser(UserRequest request) {
        if (userRepository.existsByUserNameIgnoreCase(request.getUserName())) {
            throw new AppException(ErrorCode.USERNAME_EXISTED);
        }
        if (userRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }
        if (userRepository.existsByPhoneIgnoreCase(request.getPhone())) {
            throw new AppException(ErrorCode.PHONE_EXISTED);
        }
        User user = UserMapper.toEntity(request);
        user.setEmailVerified(false);
        // tạo token + hạn 24h
        String token = generateToken();
        user.setEmailVerifyToken(token);
        user.setEmailVerifyTokenExp(java.time.LocalDateTime.now().plusHours(24));
        userRepository.save(user);
        // gửi mail kèm link xác thực
        String link = baseUrl + "/api/users/verify-email?token=" + token;
        emailService.sendEmailVerification(user.getEmail(), link);
        return UserMapper.toResponse(user);
    }
    public void verifyEmail(String token) {
        User user = userRepository.findByEmailVerifyToken(token)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_TOKEN));

        if (user.getEmailVerifyTokenExp() == null ||
                java.time.LocalDateTime.now().isAfter(user.getEmailVerifyTokenExp())) {
            throw new AppException(ErrorCode.TOKEN_EXPIRED);
        }

        user.setEmailVerified(true);
        user.setEmailVerifyToken(null);
        user.setEmailVerifyTokenExp(null);
        userRepository.save(user);
    }

    private String generateToken() {
        // token 32 ký tự, bạn có thể dùng UUID hoặc SecureRandom
        return java.util.UUID.randomUUID().toString().replace("-", "");
    }

    public void resendEmailVerify(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (user.isEmailVerified()) return; // đã xác thực thì bỏ qua

        String newToken = generateToken();
        user.setEmailVerifyToken(newToken);
        user.setEmailVerifyTokenExp(LocalDateTime.now().plusHours(24));
        userRepository.save(user);

        String link = baseUrl + "/api/users/verify-email?token=" + newToken;
        emailService.sendEmailVerification(user.getEmail(), link);
    }

    public boolean isEmailVerified(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return user.isEmailVerified();
    }

    // Tìm người dùng theo username
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

    // Tìm người dùng theo full name
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
        //rang buoc trung lap
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (userRepository.existsByEmailIgnoreCase(request.getEmail())
                && !existingUser.getEmail().equalsIgnoreCase(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }
        if (userRepository.existsByPhoneIgnoreCase(request.getPhone())
                && !existingUser.getPhone().equalsIgnoreCase(request.getPhone())) {
            throw new AppException(ErrorCode.PHONE_EXISTED);
        }

        // rang buoc de trong va null thi giu lai gia tri cu
        if (request.getFullName() != null && !request.getFullName().isBlank()) {
            existingUser.setFullName(request.getFullName());
        }
        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            existingUser.setEmail(request.getEmail());
        }
        if (request.getPhone() != null && !request.getPhone().isBlank()) {
            existingUser.setPhone(request.getPhone());
        }

        userRepository.save(existingUser);
        return UserMapper.toResponse(existingUser);
    }

    // Xóa mem user
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        user.setDelete(true); // đánh dấu đã xóa
        userRepository.save(user);
    }

    // cap nhat role nguoi dung
    public UserResponse updateUserRole(Long id, Role newRole) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        user.setRoleID(newRole);
        userRepository.save(user);

        return UserMapper.toResponse(user);
    }
}
