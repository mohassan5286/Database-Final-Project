package com.backend.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "ParkingLot")
@Data
public class ParkingLot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LotID")
    private Integer lotId;

    @Column(name = "Directions", nullable = false, length = 45)
    private String directions;

    @Column(name = "Location", nullable = false, length = 45)
    private String location;

    @Column(name = "Capacity", nullable = false)
    private Integer capacity;

    @Column(name = "Admin_AdminID", nullable = false)
    private Integer adminId; // Foreign key as an integer

    @Column(name = "Revenue", nullable = false)
    private Integer revenue = 0;
}

