package com.backend.repository;

import com.backend.dto.TopParkingSpotDTO;
import com.backend.entity.ParkingSpot;
import org.hibernate.type.descriptor.converter.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Integer> {
    @Query("SELECT " +
            "r.parkingSpotId AS lotID, " +
            "COUNT(r) * p.price AS totalRevenue " +
            "FROM Reservation r " +
            "JOIN ParkingSpot p ON r.parkingSpotId = p.spotId " +
            "GROUP BY r.parkingSpotId, p.price " +
            "ORDER BY totalRevenue DESC")
    List<Map<String, Object>> getTopParkingLots();
}
