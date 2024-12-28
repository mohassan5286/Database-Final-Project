package com.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;



@Entity
@Table(name = "User")
@IdClass(UserId.class)  // Indicate that UserId is the composite key class
@Getter
@Setter
public class User {

    @Id
    @Column(name = "idUser")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idUser;

    @Id
    @Column(name = "Admin_AdminID")
    private Integer adminId;

    @Column(name = "Debt")
    private Integer debt;

    @Column(name = "LicensePlate")
    private String licensePlate;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "PaymentMethod")
    private String paymentMethod;
}
