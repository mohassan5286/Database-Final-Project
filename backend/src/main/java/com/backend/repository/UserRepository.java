package com.backend.repository;

import com.backend.dto.TopUserDTO;
import com.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT " +
            "r.userId AS userID, " +
            "COUNT(r) AS reservationCount " +
            "FROM Reservation r " +
            "GROUP BY r.userId " +
            "ORDER BY reservationCount DESC")
    List<Map<String, Object>> getTopUsers();


    // Custom query to find a user by username
    @Query("SELECT u FROM User u WHERE u.name = :username")
    User findByUsername(@Param("username") String username);

    // Custom query to find a user by verification token
//    @Query("SELECT u FROM User u WHERE u.verificationToken = :token")
//    User findByVerificationToken(@Param("token") String token);

    // Custom query to find a user by email
    @Query("SELECT u FROM User u WHERE u.email = :email")
    User findByEmail(@Param("email") String email);

    // Custom query to find a user by email and password
    @Query("SELECT u FROM User u WHERE u.email = :email AND u.password = :password")
    User findByEmailAndPassword(@Param("email") String email, @Param("password") String password);

    // Additional custom query to find a user by email and check if they are enabled
//    @Query("SELECT u FROM User u WHERE u.email = :email AND u.enabled = true")
//    User findByEmailAndEnabled(@Param("email") String email);

}
