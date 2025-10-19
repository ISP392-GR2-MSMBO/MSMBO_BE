package com.example.ticket_booking_system.service;

import com.example.ticket_booking_system.Enum.Role;
import com.example.ticket_booking_system.auth.JwtTokenProvider;
import com.example.ticket_booking_system.dto.reponse.auth.LoginResponse;
import com.example.ticket_booking_system.dto.request.auth.LoginRequest;
import com.example.ticket_booking_system.dto.request.auth.RegisterRequest;
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
    // nếu bạn có bean BCryptPasswordEncoder trong config, inject nó thay vì new
    private final BCryptPasswordEncoder passwordEncoder;

    // 🟢 Register — chỉ cho CUSTOMER
    public LoginResponse register(RegisterRequest request) {
        if (userRepository.existsByUserNameIgnoreCase(request.getUserName())) {
            throw new AppException(ErrorCode.USERNAME_EXISTED);
        }
        if (request.getEmail() != null && userRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }
        if (request.getPhone() != null && userRepository.existsByPhoneIgnoreCase(request.getPhone())) {
            throw new AppException(ErrorCode.PHONE_EXISTED);
        }

        User user = User.builder()
                .userName(request.getUserName())
                .fullName(request.getFullName())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .phone(request.getPhone())
                .roleID(Role.CUS)
                .status(true)
                .build();

        userRepository.save(user);

        String token = jwtTokenProvider.generateToken(user.getUserName(), user.getRoleID().name());
        return new LoginResponse(token, user.getUserID(), user.getUserName(), user.getRoleID());
    }

    // 🟡 Login — tất cả role
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUserNameCaseSensitive(request.getUserName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

//        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
//            throw new AppException(ErrorCode.INVALID_PASSWORD);
//        }
        if (!request.getPassword().equals(user.getPassword())) {
            throw new AppException(ErrorCode.INVALID_PASSWORD);
        }

        String token = jwtTokenProvider.generateToken(user.getUserName(), user.getRoleID().name());
        return new LoginResponse(token, user.getUserID(), user.getUserName(), user.getRoleID());
    }
}

