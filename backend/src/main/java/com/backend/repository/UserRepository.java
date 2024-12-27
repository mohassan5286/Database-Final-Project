package com.backend.repository;

import com.backend.dto.TopParkingLotDTO;
import com.backend.dto.TopUserDTO;
import com.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("SELECT " +
            "r.parkingSpotLotID AS lotID, " +
            "SUM(COUNT(r) * p.price) AS totalRevenue " +
            "FROM Reservation r " +
            "JOIN ParkingSpot p ON r.parkingSpotSpotID = p.spotID AND r.parkingSpotLotID = p.parkingLotLotID " +
            "GROUP BY r.parkingSpotLotID " +
            "ORDER BY totalRevenue DESC")
    List<TopParkingLotDTO> getTopParkingLots();


}
