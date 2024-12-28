package com.backend.controller;

import com.backend.entity.ParkingLot;
import com.backend.repository.ParkingLotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(value = "http://localhost:3000/")
@RequestMapping("")
public class ParkingLotController {

    @Autowired
    ParkingLotRepository parkingLotRepository;

    @GetMapping("parkingLots")
    public List<ParkingLot> getParkingLots() {
        return parkingLotRepository.findAll();
    }

}
