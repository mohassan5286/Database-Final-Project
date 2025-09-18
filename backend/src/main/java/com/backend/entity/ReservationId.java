package com.backend.entity;

import java.io.Serializable;
import java.util.Objects;

public class ReservationId implements Serializable {

    private int reservationId;
    private int userId;
    private int adminId;
    private int parkingSpotId;
    private int parkingLotId;
    private int parkingLotAdminId;

    // Getters and Setters
    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public int getParkingSpotId() {
        return parkingSpotId;
    }

    public void setParkingSpotId(int parkingSpotId) {
        this.parkingSpotId = parkingSpotId;
    }

    public int getParkingLotId() {
        return parkingLotId;
    }

    public void setParkingLotId(int parkingLotId) {
        this.parkingLotId = parkingLotId;
    }

    public int getParkingLotAdminId() {
        return parkingLotAdminId;
    }

    public void setParkingLotAdminId(int parkingLotAdminId) {
        this.parkingLotAdminId = parkingLotAdminId;
    }

    // Equals and HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservationId that = (ReservationId) o;
        return reservationId == that.reservationId &&
                userId == that.userId &&
                adminId == that.adminId &&
                parkingSpotId == that.parkingSpotId &&
                parkingLotId == that.parkingLotId &&
                parkingLotAdminId == that.parkingLotAdminId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(reservationId, userId, adminId, parkingSpotId, parkingLotId, parkingLotAdminId);
    }
}
