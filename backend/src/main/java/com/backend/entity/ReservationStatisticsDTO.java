package com.backend.entity;

public class ReservationStatisticsDTO {
    private Long occupancyRate;
    private Double revenue;
    private Long violationCount;

    public ReservationStatisticsDTO(Long occupancyRate, Double revenue, Long violationCount) {
        this.occupancyRate = occupancyRate;
        this.revenue = revenue;
        this.violationCount = violationCount;
    }

    public Long getOccupancyRate() {
        return occupancyRate;
    }

    public Double getRevenue() {
        return revenue;
    }

    public Long getViolationCount() {
        return violationCount;
    }
}
