package com.backend.entity;
import com.backend.entity.Admin;
import com.backend.entity.ParkingSpot;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class ParkingLot {
    @Id
    private Integer lotID;

    private String directions;
    private String location;
    private String capacity;

    @ManyToOne
    @JoinColumn(name = "Admin_AdminID", referencedColumnName = "adminID")
    private Admin admin;

    // Getters and Setters
}
