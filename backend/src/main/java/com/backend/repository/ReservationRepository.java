package com.backend.repository;

import com.backend.entity.Reservation;
import com.backend.entity.ReservationStatisticsDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("""
        SELECT 
            new com.example.dto.ParkingLotStatisticsDTO(
                r.parkingSpot.parkingLot.lotID, 
                COUNT(r), 
                SUM(r.parkingSpot.price), 
                SUM(CASE WHEN r.arrived = 'NO' THEN 1 ELSE 0 END)
            )
        FROM Reservation r
        GROUP BY r.parkingSpot.parkingLot.lotID
    """)
    List<ReservationStatisticsDTO> getStatisticsForAllLots();
}

