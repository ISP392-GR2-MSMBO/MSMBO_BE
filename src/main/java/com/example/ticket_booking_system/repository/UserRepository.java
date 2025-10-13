package com.example.ticket_booking_system.repository;

import com.example.ticket_booking_system.entity.User;
import com.example.ticket_booking_system.Enum.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByUserNameContainingIgnoreCase(String keyword);;
    boolean existsByUserNameIgnoreCase(String userName);
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByPhoneIgnoreCase(String phone);
    List<User> findByFullNameContainingIgnoreCase(String keyword);
}

