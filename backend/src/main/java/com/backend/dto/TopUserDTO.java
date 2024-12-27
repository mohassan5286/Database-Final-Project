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

    // Getters and Setters
}
