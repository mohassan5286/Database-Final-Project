package com.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Reservation")
@Data
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ReservationID")
    private Integer reservationId;

    @Column(name = "Arrived", nullable = false, length = 45)
    private String arrived = "No";

    @Column(name = "StartTime", nullable = false, length = 45)
    private String startTime;

    @Column(name = "EndTime", nullable = false, length = 45)
    private String endTime;

    @Column(name = "User_idUser", nullable = false)
    private Integer userId; // Foreign key as a plain integer

    @Column(name = "ParkingSpot_SpotID", nullable = false)
    private Integer parkingSpotId; // Foreign key as a plain integer
}
