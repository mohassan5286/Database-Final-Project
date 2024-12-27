package com.backend.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "Admin", schema = "ParkingSystemSchema")
public class Admin {
    @Id
    private int adminID;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "admin")
    private List<User> users;

    @OneToMany(mappedBy = "admin")
    private List<ParkingLot> parkingLots;

    // Getters and Setters
}
