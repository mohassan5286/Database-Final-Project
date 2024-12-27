package com.backend.entity;

import com.backend.entity.Reservation;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class ParkingSpot {
    @Id
    private Integer spotID;

    private String type;
    private String status;
    private Integer price;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "ParkingLot_LotID", referencedColumnName = "lotID"),
//            @JoinColumn(name = "ParkingLot_Admin_AdminID", referencedColumnName = "adminID")
    })
    private ParkingLot parkingLot;

    // Getters and Setters
}
