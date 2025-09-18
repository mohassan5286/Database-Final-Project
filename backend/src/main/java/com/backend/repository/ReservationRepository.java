package com.backend.repository;

import com.backend.dto.ReservationStatisticsDTO;
import com.backend.entity.Reservation;
import com.backend.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    @Query("""
        SELECT r.userId 
        FROM Reservation r
        WHERE r.parkingSpotId = :spotId
        ORDER BY r.reservationId DESC
        LIMIT 1
    """)
    Integer findLatestUserBySpotIdAndStatus(@Param("spotId") Integer spotId);

    Optional<Reservation> findByUserIdAndParkingSpotId(Integer userId, Integer parkingSpotId);

    @Procedure(name = "ReserveParkingSpot")
    void reserveParkingSpot(
            @Param("spotId") Integer spotId,
            @Param("userId") Integer userId,
            @Param("startTime") String startTime,
            @Param("endTime") String endTime
    );
}

