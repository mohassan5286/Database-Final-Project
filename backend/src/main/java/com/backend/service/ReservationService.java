package com.backend.service;

import com.backend.entity.ParkingLot;
import com.backend.entity.ParkingSpot;
import com.backend.entity.Reservation;
import com.backend.entity.User;
import com.backend.repository.ParkingLotRepository;
import com.backend.repository.ParkingSpotRepository;
import com.backend.repository.ReservationRepository;
import com.backend.repository.UserRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;

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

    @Transactional
    public String createReservation(Reservation reservation) throws Exception {

        if( reservationRepository.findById(reservation.getReservationId()).isPresent() )
            throw new Exception("Already Reserved Spot");
        System.out.println(reservation.toString());
        User user = userRepository.findById(reservation.getUserId()).orElseThrow(() ->
                new NoSuchElementException("No Such User")
        );;

        ParkingSpot parkingSpot = parkingSpotRepository.findById(reservation.getParkingSpotId()).orElseThrow(() ->
            new NoSuchElementException("No Such Spot")
        );

        ParkingLot parkingLot = parkingLotRepository.findById(parkingSpot.getParkingLotId()).orElseThrow(() ->
                new NoSuchElementException("No Such Lot")
        );;

        double userDebt = user.getDebt();

        if (userDebt > 0) {

            parkingLot.setRevenue((int) (parkingLot.getRevenue()+userDebt));

            user.setDebt(0);

        }

        double dynamicPrice = calculateDynamicPrice(reservation, parkingSpot.getPrice());
        user.setDebt((int) dynamicPrice);

        parkingSpot.setStatus("Reserved");
        System.out.println(1);
        userRepository.save(user);
        System.out.println(2);
        parkingSpotRepository.save(parkingSpot);
        parkingLotRepository.save(parkingLot);
        System.out.println(3);
        reservation.setReservationId(null);
        reservationRepository.save(reservation);
        System.out.println(4);
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

    public void userArrived(Integer userId, Integer spotId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        ParkingSpot parkingSpot = parkingSpotRepository.findById(spotId).orElseThrow(() ->
                new NoSuchElementException("No Such Spot")
        );

        ParkingLot parkingLot = parkingLotRepository.findById(parkingSpot.getParkingLotId()).orElseThrow(() ->
                new NoSuchElementException("No Such Lot")
        );

        Reservation reservation = reservationRepository.findByUserIdAndParkingSpotId(userId, spotId).orElseThrow(
                () ->   new NoSuchElementException("Slot is not reserved")
        );

        LocalTime endTimeOfDay = LocalTime.parse(reservation.getEndTime(), formatter);


        if ("No".equalsIgnoreCase(reservation.getArrived()) && LocalTime.now().isBefore(endTimeOfDay) ) {
            double userDebt = user.getDebt();
            double currentRevenue = parkingLot.getRevenue();

            parkingLot.setRevenue((int) (currentRevenue + userDebt));
            user.setDebt(0);
            reservation.setArrived("Yes");
            parkingSpot.setStatus("Occupied");

            userRepository.save(user);
            parkingSpotRepository.save(parkingSpot);
            parkingLotRepository.save(parkingLot);
            reservationRepository.save(reservation);
        } else if ( "Yes".equalsIgnoreCase(reservation.getArrived()) ) {
            throw new IllegalStateException("User has not arrived yet");
        } else {
            throw new IllegalStateException("Are you waiting for something ... An Error ... Good Luck");
        }
    }


}
