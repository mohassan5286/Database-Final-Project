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
//    @Query("SELECT " +
//            "r.parkingSpot.parkingLot.lotId AS lotID, " + // Corrected the path for the foreign keys
//            "COUNT(r) * p.price AS totalRevenue " +
//            "FROM Reservation r " +
//            "JOIN ParkingSpot p ON r.parkingSpot.spotId = p.spotId " + // Adjusted the join condition
//            "WHERE r.parkingSpot.parkingLot.lotId = p.parkingLot.lotId " + // Corrected foreign key relation
//            "GROUP BY r.parkingSpot.parkingLot.lotId, p.price " + // Group by parkingLot and price
//            "ORDER BY totalRevenue DESC")
//    List<TopParkingSpotDTO> getTopParkingLots();
}
