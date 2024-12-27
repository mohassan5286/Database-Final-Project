package com.backend.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "User", schema = "ParkingSystemSchema")
public class User {
    @Id
    private int idUser;

    private int debt;

    @Column(nullable = false)
    private String licensePlate;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String paymentMethod;

    @ManyToOne
    @JoinColumn(name = "Admin_AdminID", nullable = false)
    private Admin admin;

    @OneToMany(mappedBy = "user")
    private List<Reservation> reservations;

    // Getters and Setters
}

