package com.backend.controller;

import com.backend.entity.Reservation;
import com.backend.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping("/create")
    public ResponseEntity<String> createReservation(@RequestBody Reservation reservation) {
        try {
            String response = reservationService.createReservation(reservation);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error creating reservation: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/arrived")
    public ResponseEntity<String> userArrived(@RequestBody Map<String, Object> arrivalDetails) {
        try {

            Integer spotId = Integer.valueOf(arrivalDetails.get("spotId").toString());
            Integer userId = Integer.valueOf(arrivalDetails.get("userId").toString());
            System.out.println(userId);
            System.out.println(spotId);
            System.out.println(143);
            reservationService.userArrived(userId, spotId);
            return new ResponseEntity<>("User arrival processed successfully", HttpStatus.OK);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error processing user arrival: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
