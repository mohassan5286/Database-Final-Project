package com.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Reservation")
@Data
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reservationID;

    private String arrived;
    private String startTime;
    private String endTime;

    @ManyToOne
    @JoinColumn(name = "user_id_user", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "parking_spot_spotid", nullable = false)
    private ParkingSpot parkingSpot;

    // Getters and Setters
}