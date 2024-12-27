package com.backend.dto;

public class TopUserDTO {
    private Integer userID;
    private Integer adminID;
    private Long reservationCount;

    public TopUserDTO(Integer userID, Integer adminID, Long reservationCount) {
        this.userID = userID;
        this.adminID = adminID;
        this.reservationCount = reservationCount;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Integer getAdminID() {
        return adminID;
    }

    public void setAdminID(Integer adminID) {
        this.adminID = adminID;
    }

    public Long getReservationCount() {
        return reservationCount;
    }

    public void setReservationCount(Long reservationCount) {
        this.reservationCount = reservationCount;
    }
    // Getters and Setters
}
