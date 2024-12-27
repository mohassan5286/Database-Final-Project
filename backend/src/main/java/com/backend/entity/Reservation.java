package com.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Reservation {
    @Id
    private Integer reservationID;

    private String arrived;
    private String startTime;
    private String endTime;

    private Integer userIdUser;             // Foreign key to User
    private Integer userAdminAdminID;       // Foreign key to User's Admin

    private Integer parkingSpotSpotID;      // Foreign key to ParkingSpot
    private Integer parkingSpotLotID;       // Foreign key to ParkingSpot's Lot
    private Integer parkingSpotAdminID;     // Foreign key to ParkingSpot's Admin

    // Getters and Setters
}
