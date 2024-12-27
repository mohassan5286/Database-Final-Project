package com.backend.dto;

public class TopParkingLotDTO {
    private Integer lotID;
    private Double totalRevenue;

    public TopParkingLotDTO(Integer lotID, Double totalRevenue) {
        this.lotID = lotID;
        this.totalRevenue = totalRevenue;
    }

    // Getters and Setters
}
