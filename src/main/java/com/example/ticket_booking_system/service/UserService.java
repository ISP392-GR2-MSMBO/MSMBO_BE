package com.example.ticket_booking_system.service;

import com.example.ticket_booking_system.Enum.Role;
import com.example.ticket_booking_system.dto.request.user.ChangePasswordRequest;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.ticket_booking_system.dto.request.auth.ForgotPasswordRequest;
import com.example.ticket_booking_system.dto.request.auth.ResetPasswordRequest;
import java.util.Optional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final BCryptPasswordEncoder passwordEncoder;
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

        // Ghi đè mật khẩu gốc bằng mật khẩu đã mã hóa
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmailVerified(true);
        user.setEmailVerifyToken(null); // Không cần token
        user.setEmailVerifyTokenExp(null);
        userRepository.save(user);
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
        user.setEmailVerifyTokenExp(LocalDateTime.now().plusMinutes(20));
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

    /**
     * CHỨC NĂNG MỚI: Đổi mật khẩu (khi đã đăng nhập)
     */
    public void changePassword(Long userId, ChangePasswordRequest request) {
        // 1. Tìm User
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // 2. Kiểm tra mật khẩu cũ có khớp không
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.INVALID_PASSWORD); // Dùng lại ErrorCode này
        }

        // 3. Mã hóa và set mật khẩu mới
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // 4. Lưu lại
        userRepository.save(user);
    }

    /**
     * CHỨC NĂNG MỚI:Yêu cầu reset mật khẩu (A)
     */
    public void requestPasswordReset(ForgotPasswordRequest request) {
        // 1. Tìm user bằng email
        Optional<User> userOpt = userRepository.findByEmailIgnoreCase(request.getEmail());

        // 2. Dù có tìm thấy hay không, KHÔNG báo lỗi
        // (Để bảo mật, tránh bị dò email)
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // 3. Tạo token và thời hạn (tái sử dụng generateToken() và trường DB)
            String token = generateToken();
            user.setEmailVerifyToken(token); // Tận dụng trường token cũ
            user.setEmailVerifyTokenExp(LocalDateTime.now().plusMinutes(10));
            userRepository.save(user);

            // 4. Tạo link (Link này trỏ về Frontend của bạn)
            // Ví dụ: http://localhost:3000/reset-password?token=... sau nay doi ten local host thi doi lại link nay
            String link = "http://localhost:3000/reset-password?token=" + token;

            // 5. Gửi email
            emailService.sendPasswordResetEmail(user.getEmail(), link);
        }
        // Nếu không tìm thấy email, ta im lặng và không làm gì cả.
    }

    /**
     * CHỨC NĂNG MỚI: Thực hiện reset mật khẩu (B)
     */
    public void resetPassword(ResetPasswordRequest request) {
        // 1. Tìm user bằng token
        User user = userRepository.findByEmailVerifyToken(request.getToken())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_TOKEN));

        // 2. Kiểm tra token hết hạn (giống hệt logic verifyEmail)
        if (user.getEmailVerifyTokenExp() == null ||
                java.time.LocalDateTime.now().isAfter(user.getEmailVerifyTokenExp())) {
            throw new AppException(ErrorCode.TOKEN_EXPIRED);
        }

        // 3. Mã hóa và đặt mật khẩu mới
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // 4. Vô hiệu hóa token
        user.setEmailVerifyToken(null);
        user.setEmailVerifyTokenExp(null);

        userRepository.save(user);
    }
}
