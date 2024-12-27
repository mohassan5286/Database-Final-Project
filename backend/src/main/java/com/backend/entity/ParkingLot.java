package com.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class ParkingLot {
    @Id
    private Integer lotID;

    private String directions;
    private String location;
    private String capacity;

    private Integer adminAdminID; // Foreign key to Admin

    // Getters and Setters
}
