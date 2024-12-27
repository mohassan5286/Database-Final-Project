package com.backend.repository;

import com.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

//    @Query("SELECT r.user, COUNT(r) as reservationCount FROM Reservation r GROUP BY r.user ORDER BY reservationCount DESC")
//    List<Object[]> getTopUsers();
//
//    @Query("SELECT r.parkingLot, SUM(r.amountPaid + r.penaltyPaid) as totalRevenue FROM Reservation r GROUP BY r.parkingLot ORDER BY totalRevenue DESC")
//    List<Object[]> getTopParkingLots();
}
