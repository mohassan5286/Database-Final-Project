package com.backend.repository;

import com.backend.dto.TopUserDTO;
import com.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT " +
            "r.userIdUser AS userID, " +
            "r.userAdminAdminID AS adminID, " +
            "COUNT(r) AS reservationCount " +
            "FROM Reservation r " +
            "GROUP BY r.userIdUser, r.userAdminAdminID " +
            "ORDER BY reservationCount DESC")
    List<TopUserDTO> getTopUsers();
}
