package com.backend.repository;

import com.backend.dto.TopParkingSpotDTO;
import com.backend.entity.ParkingSpot;
import org.hibernate.type.descriptor.converter.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Integer> {
    @Query("SELECT " +
            "r.parkingSpotLotID AS lotID, " +
            "COUNT(r) * p.price AS totalRevenue " +
            "FROM Reservation r " +
            "JOIN ParkingSpot p ON r.parkingSpotSpotID = p.spotID AND r.parkingSpotLotID = p.parkingLotLotID " +
            "GROUP BY r.parkingSpotLotID, p.price " +
            "ORDER BY totalRevenue DESC")
    List<TopParkingSpotDTO> getTopParkingLots();
}
