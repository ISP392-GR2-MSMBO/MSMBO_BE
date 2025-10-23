package com.example.ticket_booking_system.repository;

import com.example.ticket_booking_system.entity.User;
import com.example.ticket_booking_system.Enum.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByUserNameContainingIgnoreCase(String keyword);;
    @Query(value = """
        SELECT TOP 1 * 
        FROM dbo.tblUsers 
        WHERE userName = :username COLLATE Latin1_General_CS_AS
    """, nativeQuery = true)
    Optional<User> findByUserNameCaseSensitive(@Param("username") String username);
    boolean existsByUserNameIgnoreCase(String userName);
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByPhoneIgnoreCase(String phone);
    List<User> findByFullNameContainingIgnoreCase(String keyword);
    List<User> findByIsDeleteFalse();
    Optional<User> findByEmailVerifyToken(String token);
}

