package com.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "ParkingSpot")
@Data
public class ParkingSpot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SpotID")
    private Integer spotId;

    @Column(name = "Type", nullable = false, length = 45)
    private String type = "Standard";

    @Column(name = "Status", nullable = false, length = 45)
    private String status = "Available";

    @Column(name = "Price", nullable = false)
    private Integer price = 0;

    @Column(name = "ParkingLot_LotID", nullable = false)
    private Integer parkingLotId; // Foreign key as an integer
}
