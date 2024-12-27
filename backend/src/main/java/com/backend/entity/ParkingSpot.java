package com.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class ParkingSpot {
    @Id
    private Integer spotID;

    private String type;
    private String status;
    private Integer price;

    private Integer parkingLotLotID;         // Foreign key to ParkingLot
    private Integer parkingLotAdminAdminID; // Foreign key to ParkingLot's Admin

    // Getters and Setters
}
