package com.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
public class ReservationStatisticsDTO {
    private Integer lotID;
    private Long reservationCount;
    private Double totalRevenue;
    private Long violationCount;

    public ReservationStatisticsDTO() {
    }

    public ReservationStatisticsDTO(Integer lotID) {
        this.lotID = lotID;
    }

    public ReservationStatisticsDTO(Integer lotID, Long reservationCount, Double totalRevenue, Long violationCount) {
        this.lotID = lotID;
        this.reservationCount = reservationCount;
        this.totalRevenue = totalRevenue;
        this.violationCount = violationCount;
    }

    public Integer getLotID() {
        return lotID;
    }

    public void setLotID(Integer lotID) {
        this.lotID = lotID;
    }

    public Long getReservationCount() {
        return reservationCount;
    }

    public void setReservationCount(Long reservationCount) {
        this.reservationCount = reservationCount;
    }

    public Double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(Double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public Long getViolationCount() {
        return violationCount;
    }

    public void setViolationCount(Long violationCount) {
        this.violationCount = violationCount;
    }
}

