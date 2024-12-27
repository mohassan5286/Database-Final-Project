package com.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class User {
    @Id
    private Integer idUser;

    private Integer debt;
    private String licensePlate;
    private String name;
    private String password;
    private String paymentMethod;

    private Integer adminAdminID; // Foreign key to Admin

    // Getters and Setters
}
