package com.backend.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "ParkingLot", schema = "ParkingSystemSchema")
public class ParkingLot {
    @Id
    private int lotID;

    @Column(nullable = false)
    private String directions;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String capacity;

    @ManyToOne
    @JoinColumn(name = "Admin_AdminID", nullable = false)
    private Admin admin;

    @OneToMany(mappedBy = "parkingLot")
    private List<ParkingSpot> parkingSpots;

    // Getters and Setters
}
