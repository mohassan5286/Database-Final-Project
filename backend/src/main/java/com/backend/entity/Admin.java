package com.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Admin {
    @Id
    private Integer adminID;

    private String name;
    private String email;
    private String password;

    // Getters and Setters
}
