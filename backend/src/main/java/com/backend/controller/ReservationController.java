package com.backend.controller;

import com.backend.entity.Reservation;
import com.backend.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<String> userArrived(@RequestBody Reservation reservation) {
        try {
            reservationService.userArrived(reservation);
            return new ResponseEntity<>("User arrival processed successfully", HttpStatus.OK);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error processing user arrival: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
