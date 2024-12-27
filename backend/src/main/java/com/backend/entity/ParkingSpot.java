package com.backend.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "ParkingSpot", schema = "ParkingSystemSchema")
public class ParkingSpot {
    @Id
    private int spotID;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private int price;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "ParkingLot_LotID", referencedColumnName = "LotID"),
            @JoinColumn(name = "ParkingLot_Admin_AdminID", referencedColumnName = "Admin_AdminID")
    })
    private ParkingLot parkingLot;

    @OneToMany(mappedBy = "parkingSpot")
    private List<Reservation> reservations;

    // Getters and Setters
}
