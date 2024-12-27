package com.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Reservation", schema = "ParkingSystemSchema")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reservationID;

    @Column(nullable = false)
    private String arrived;

    @Column(nullable = false)
    private String startTime;

    @Column(nullable = false)
    private String endTime;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "User_idUser", referencedColumnName = "idUser"),
            @JoinColumn(name = "User_Admin_AdminID", referencedColumnName = "Admin_AdminID")
    })
    private User user;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "ParkingSpot_SpotID", referencedColumnName = "SpotID"),
            @JoinColumn(name = "ParkingSpot_ParkingLot_LotID", referencedColumnName = "LotID"),
            @JoinColumn(name = "ParkingSpot_ParkingLot_Admin_AdminID", referencedColumnName = "Admin_AdminID")
    })
    private ParkingSpot parkingSpot;

    // Getters and Setters
}
