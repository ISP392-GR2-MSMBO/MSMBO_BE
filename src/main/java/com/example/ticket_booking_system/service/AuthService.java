package com.example.ticket_booking_system.service;

import com.example.ticket_booking_system.Enum.Role;
import com.example.ticket_booking_system.auth.JwtTokenProvider;
import com.example.ticket_booking_system.dto.reponse.auth.LoginResponse;
import com.example.ticket_booking_system.dto.request.auth.LoginRequest;
import com.example.ticket_booking_system.dto.request.auth.RegisterRequest;
import com.example.ticket_booking_system.dto.reponse.user.UserResponse;
import com.example.ticket_booking_system.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Value;
import java.time.LocalDateTime;
import com.example.ticket_booking_system.entity.User;
import com.example.ticket_booking_system.exception.AppException;
import com.example.ticket_booking_system.exception.ErrorCode;
import com.example.ticket_booking_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final BCryptPasswordEncoder passwordEncoder;

    private final EmailService emailService;
    @Value("${app.base-url}")
    private String baseUrl;

    // 🟢 Register — chỉ cho CUSTOMER
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByUserNameIgnoreCase(request.getUserName())) {
            throw new AppException(ErrorCode.USERNAME_EXISTED);
        }
        if (request.getEmail() != null && userRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }
        if (request.getPhone() != null && userRepository.existsByPhoneIgnoreCase(request.getPhone())) {
            throw new AppException(ErrorCode.PHONE_EXISTED);
        }

        String token = generateToken();

        User user = User.builder()
                .userName(request.getUserName())
                .fullName(request.getFullName())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .phone(request.getPhone())
                .roleID(Role.CUS)
                .status(true)
                .emailVerified(false)
                .emailVerifyToken(token)
                .emailVerifyTokenExp(LocalDateTime.now().plusMinutes(20))
                .build();

        User savedUser = userRepository.save(user);

        // --- LOGIC MỚI: GỬI EMAIL ---
        String link = baseUrl + "/api/users/verify-email?token=" + token;
        emailService.sendEmailVerification(savedUser.getEmail(), link);

        // trả về thông tin user đã tạo
        return UserMapper.toResponse(savedUser);
    }

    // 🟡 Login — tất cả role
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUserNameCaseSensitive(request.getUserName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.INVALID_PASSWORD);
        }

        if (!user.isEmailVerified()) {
            // Dùng ErrorCode mới tạo
            throw new AppException(ErrorCode.EMAIL_NOT_VERIFIED);
        }
        // --- KẾT THÚC KIỂM TRA MỚI ---

        String token = jwtTokenProvider.generateToken(user.getUserName(), user.getRoleID().name());
        return new LoginResponse(token, user.getUserID(), user.getUserName(), user.getRoleID());
    }

    // --- HÀM MỚI: Dùng để tạo token (copy từ UserService) ---
    private String generateToken() {
        return java.util.UUID.randomUUID().toString().replace("-", "");
    }
}

