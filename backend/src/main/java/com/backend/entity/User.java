package com.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "User", schema = "ParkingSystemSchema")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUser", nullable = false)
    private Integer idUser;

    @Id
    @Column(name = "Admin_AdminID", nullable = false)
    private Integer adminId;

    @Column(name = "Debt")
    private Integer debt;

    @Column(name = "LicensePlate", nullable = false, length = 45)
    private String licensePlate;

    @Column(name = "Name", nullable = false, length = 45)
    private String name;

    @Column(name = "Password", nullable = false, length = 45)
    private String password;

    @Column(name = "PaymentMethod", nullable = false, length = 45)
    private String paymentMethod;

    // Getters and Setters
    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public Integer getDebt() {
        return debt;
    }

    public void setDebt(Integer debt) {
        this.debt = debt;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}

