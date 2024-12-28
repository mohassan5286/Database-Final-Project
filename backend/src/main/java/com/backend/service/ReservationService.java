package com.backend.service;

import com.backend.entity.ParkingLot;
import com.backend.entity.Reservation;
import com.backend.entity.User;
import com.backend.repository.ParkingLotRepository;
import com.backend.repository.ParkingSpotRepository;
import com.backend.repository.ReservationRepository;
import com.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ParkingSpotRepository parkingSpotRepository;
    @Autowired
    private ParkingLotRepository parkingLotRepository;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public String createReservation(Reservation reservation) {
        User user = userRepository.findById(reservation.getUser().getIdUser()).get();

        ParkingLot parkingLot = parkingLotRepository.findById(reservation.getParkingSpotId()).get();

        double userDebt = user.getDebt();

        if (userDebt > 0) {

            parkingLot.setRevenue((int) (parkingLot.getRevenue()+userDebt));

            user.setDebt(0);

        }

        double dynamicPrice = calculateDynamicPrice(reservation, parkingSpotRepository.findById(reservation.getParkingSpotId()).get().getPrice());
        user.setDebt((int) dynamicPrice);

        userRepository.save(user);
        parkingLotRepository.save(parkingLot);
        reservationRepository.save(reservation);
        return "Reservation is successfully done";
    }


    private double calculateDynamicPrice(Reservation reservation, double basePrice) {
        int spotFactor = Math.toIntExact(reservation.getParkingSpotId() % 3);
//        int activeUsersCount = getActiveUsersCount();
        int activeUsersCount = 5;
        LocalTime startTimeOfDay = LocalTime.parse(reservation.getStartTime(), formatter);
        LocalTime endTimeOfDay = LocalTime.parse(reservation.getEndTime(), formatter);

        double timeFactor;
        if (startTimeOfDay.isAfter(LocalTime.of(12, 0)) && startTimeOfDay.isBefore(LocalTime.of(18, 0))) {
            timeFactor = 1.5;
        } else if (startTimeOfDay.isAfter(LocalTime.of(5, 0)) && startTimeOfDay.isBefore(LocalTime.of(12, 0))) {
            timeFactor = 1.1;
        } else {
            timeFactor = 0.9;
        }

        long durationInHours = Duration.between(startTimeOfDay, endTimeOfDay).toHours();
        double durationFactor = Math.max(1.0, durationInHours * 0.1);

        return basePrice + spotFactor + activeUsersCount * 0.2 + basePrice * timeFactor * durationFactor;
    }

    public void userArrived(Reservation reservation) {
        User user = userRepository.findById(reservation.getUserId()).orElseThrow(() -> new IllegalArgumentException("User not found"));

        LocalTime endTimeOfDay = LocalTime.parse(reservation.getEndTime(), formatter);

        ParkingLot parkingLot = parkingLotRepository.findById(reservation.getParkingSpotId()).get();

        if ("Yes".equalsIgnoreCase(reservation.getArrived()) && LocalTime.now().isAfter(endTimeOfDay) ) {
            double userDebt = user.getDebt();
            double currentRevenue = parkingLot.getRevenue();

            parkingLot.setRevenue((int) (currentRevenue + userDebt));
            user.setDebt(0);

            userRepository.save(user);
            parkingLotRepository.save(parkingLot);
            reservationRepository.save(reservation);
        } else {
            throw new IllegalStateException("User has not arrived yet");
        }
    }


}
