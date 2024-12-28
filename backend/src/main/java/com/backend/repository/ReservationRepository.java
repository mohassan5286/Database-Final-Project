package com.backend.repository;

import com.backend.dto.ReservationStatisticsDTO;
import com.backend.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    @Query("SELECT " +
            "r.parkingSpotId AS lotID, " +
            "COUNT(r) AS reservationCount, " +
            "SUM(ps.price) AS totalRevenue, " +
            "SUM(CASE WHEN r.arrived = 'NO' THEN 1 ELSE 0 END) AS violationCount " +
            "FROM Reservation r " +
            "JOIN ParkingSpot ps " +
            "ON r.parkingSpotId = ps.spotId " +
            "GROUP BY r.parkingSpotId")
    List<Map<String, Object>>    getStatisticsForAllLots();
}

