package com.backend.dto;

public class TopParkingSpotDTO {
    private Integer lotID;
    private Double totalRevenue;

    public TopParkingSpotDTO(Integer lotID, Double totalRevenue) {
        this.lotID = lotID;
        this.totalRevenue = totalRevenue;
    }

    public Integer getLotID() {
        return lotID;
    }

    public void setLotID(Integer lotID) {
        this.lotID = lotID;
    }

    public Double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(Double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
    // Getters and Setters
}
