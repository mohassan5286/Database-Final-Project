package com.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

@Entity
public class User {
    @Id
    private Integer idUser;

    private Integer debt;
    private String licensePlate;
    private String name;
    private String password;
    private String paymentMethod;

    @ManyToOne
    @JoinColumn(name = "Admin_AdminID", referencedColumnName = "adminID")
    private Admin admin;

    // Getters and Setters
}