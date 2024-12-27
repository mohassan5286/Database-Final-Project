package com.backend.entity;

import jakarta.persistence.*;

@Entity
public class Reservation {
    @Id
    private Integer reservationID;

    private String arrived;
    private String startTime;
    private String endTime;

//    @ManyToOne
//    @JoinColumns({
//            @JoinColumn(name = "User_idUser", referencedColumnName = "idUser"),
//            @JoinColumn(name = "User_Admin_AdminID", referencedColumnName = "adminID")
//    })
//    private User user;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "ParkingSpot_SpotID", referencedColumnName = "spotID"),
            @JoinColumn(name = "ParkingSpot_ParkingLot_LotID", referencedColumnName = "ParkingLot_LotID"),
//            @JoinColumn(name = "ParkingSpot_ParkingLot_Admin_AdminID", referencedColumnName = "adminID")
    })
    private ParkingSpot parkingSpot;

    // Getters and Setters
}