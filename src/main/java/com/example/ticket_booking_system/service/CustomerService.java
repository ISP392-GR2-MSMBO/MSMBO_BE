package com.example.ticket_booking_system.service;

import com.example.ticket_booking_system.Enum.Role;
import com.example.ticket_booking_system.dto.reponse.customer.CustomerResponse;
import com.example.ticket_booking_system.dto.request.customer.CustomerRequest;
import com.example.ticket_booking_system.dto.request.customer.CustomerUpdateProfileRequest;
import com.example.ticket_booking_system.entity.Customer;
import com.example.ticket_booking_system.entity.User;
import com.example.ticket_booking_system.exception.AppException;
import com.example.ticket_booking_system.exception.ErrorCode;
import com.example.ticket_booking_system.mapper.CustomerMapper;
import com.example.ticket_booking_system.repository.CustomerRepository;
import com.example.ticket_booking_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    // 🟢 CREATE CUSTOMER (Register)
    public CustomerResponse createCustomer(CustomerRequest request) {
        var userReq = request.getUserRequest();

        if (userRepository.existsByEmailIgnoreCase(userReq.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }
        if (userRepository.existsByPhoneIgnoreCase(userReq.getPhone())) {
            throw new AppException(ErrorCode.PHONE_EXISTED);
        }

        User user = new User();
        user.setUserName(userReq.getUserName());
        user.setFullName(userReq.getFullName());
        user.setEmail(userReq.getEmail());
        user.setPhone(userReq.getPhone());
        user.setPassword(userReq.getPassword());
        user.setRoleID(Role.CUS);
        user.setStatus(true);

        userRepository.save(user);

        Customer customer = CustomerMapper.toEntity(request);
        customer.setUser(user);
        customerRepository.save(customer);

        return CustomerMapper.toResponse(customer);
    }

    //  READ (View profile)
    public CustomerResponse getCustomerProfile(Long userId) {
        Customer customer = customerRepository.findByUser_UserID(userId)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_NOT_FOUND));
        return CustomerMapper.toResponse(customer);
    }

    //  UPDATE (Edit profile)
    public CustomerResponse updateProfile(Long userId, CustomerUpdateProfileRequest request) {
        Customer existing = customerRepository.findByUser_UserID(userId)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_NOT_FOUND));
        User existingUser = existing.getUser();

        if (request.getEmail() != null &&
                !request.getEmail().equalsIgnoreCase(existingUser.getEmail()) &&
                userRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }

        if (request.getPhone() != null &&
                !request.getPhone().equalsIgnoreCase(existingUser.getPhone()) &&
                userRepository.existsByPhoneIgnoreCase(request.getPhone())) {
            throw new AppException(ErrorCode.PHONE_EXISTED);
        }

        if (request.getFullName() != null) existingUser.setFullName(request.getFullName());
        if (request.getEmail() != null) existingUser.setEmail(request.getEmail());
        if (request.getPhone() != null) existingUser.setPhone(request.getPhone());

        if (request.getGender() != null) existing.setGender(request.getGender());
        if (request.getAddress() != null) existing.setAddress(request.getAddress());
        if (request.getDateOfBirth() != null) existing.setDateOfBirth(request.getDateOfBirth());

        userRepository.save(existingUser);
        customerRepository.save(existing);

        return CustomerMapper.toResponse(existing);
    }

    // DELETE (Soft delete: set status = false)
    public void deleteCustomer(Long userId) {
        Customer customer = customerRepository.findByUser_UserID(userId)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_NOT_FOUND));
        User user = customer.getUser();

        user.setStatus(false); // set inactive
        userRepository.save(user);
    }
}
