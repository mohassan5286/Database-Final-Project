package com.backend.controller;

import com.backend.entity.ParkingLot;
import com.backend.entity.User;
import com.backend.repository.ParkingLotRepository;
import com.backend.repository.ParkingSpotRepository;
import com.backend.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("")
public class ParkingLotController {

    @Autowired
    ParkingLotRepository parkingLotRepository;

    @Autowired
    ParkingSpotRepository parkingSpotRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @GetMapping("/parkingLots")
    public List<Map<String, Object>> getParkingLots() {
        String user_id = "";
        return parkingLotRepository.findAll().stream().map(parkingLot -> {
            Map<String, Object> parkingLotMap = new HashMap<>();
            parkingLotMap.put("lot_id", parkingLot.getLotId());
            parkingLotMap.put("location", parkingLot.getLocation());
            parkingLotMap.put("admin_id", parkingLot.getAdminId()); // Assuming `getAdmin()` returns the related Admin object
            parkingLotMap.put("directions", parkingLot.getDirections()); // Static example

            List<Map<String, Object>> spots = parkingSpotRepository.findByParkingLotId(parkingLot.getLotId()).stream().map(spot -> {
                Map<String, Object> spotMap = new HashMap<>();
                spotMap.put("spot_id", spot.getSpotId());
                spotMap.put("spot_type", spot.getType()); // Assuming `getType()` gives "Regular", "Disabled", etc.
                spotMap.put("status", spot.getStatus()); // "Available", "Occupied", etc.
                spotMap.put("user_id", reservationRepository.findLatestUserBySpotIdAndStatus(spot.getSpotId()) == null? "" : reservationRepository.findLatestUserBySpotIdAndStatus(spot.getSpotId())); // Assuming `getUserId()` provides user ID or null
                return spotMap;
            }).collect(Collectors.toList());

            parkingLotMap.put("spots", spots);
            return parkingLotMap;
        }).collect(Collectors.toList());
    }

}
