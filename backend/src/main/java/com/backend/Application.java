package com.backend;

import com.backend.entity.Admin;
import com.backend.entity.ParkingLot;
import com.backend.entity.ParkingSpot;
import com.backend.entity.Reservation;
import com.backend.repository.ParkingLotRepository;
import com.backend.repository.ParkingSpotRepository;
import com.backend.repository.ReservationRepository;
import com.backend.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;


@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

//	@Bean
//	public CommandLineRunner insertDummyData(ReservationRepository reservationRepository) {
//		return args -> {
//			// Create a new Reservation object with dummy data
//			Reservation reservation = new Reservation();
//			reservation.setArrived("Yes"); // Adjusted to match your data type
//			reservation.setStartTime("2024-12-28T10:30:00");
//			reservation.setEndTime("2024-12-28T12:00:00");
//			reservation.setUserId(1); // Foreign key to User
//			reservation.setParkingSpotId(1); // Foreign key to ParkingSpot
//
//			// Save the reservation into the database
//			reservationRepository.save(reservation);
//
//			System.out.println("Dummy Reservation saved!");
//		};
//	};

//	@Bean
//	public CommandLineRunner insertDummyData(ParkingSpotRepository parkingSpotRepository) {
//		return args -> {
//			// Create a new ParkingSpot object with dummy data
//			ParkingSpot parkingSpot = new ParkingSpot();
//			parkingSpot.setType("Standard"); // Adjusted to match your data type
//			parkingSpot.setStatus("Available");
//			parkingSpot.setPrice(10); // Example price
//			parkingSpot.setParkingLotId(1); // Foreign key to ParkingLot (assuming you have a ParkingLot entity)
//
//			// Save the parking spot into the database
//			parkingSpotRepository.save(parkingSpot);
//
//			System.out.println("Dummy ParkingSpot saved!");
//		};
//	}
//	@Bean
//	public CommandLineRunner insertDummyData(ParkingLotRepository parkingLotRepository) {
//		return args -> {
//			// Create a new ParkingLot object with dummy data
//			ParkingLot parkingLot = new ParkingLot();
//			parkingLot.setDirections("Main St, Downtown");
//			parkingLot.setLocation("123 Main Street, City");
//			parkingLot.setCapacity("200"); // Example capacity
//			parkingLot.setAdminId(1); // Foreign key to Admin (assuming you have an Admin entity)
//			parkingLot.setRevenue(5000); // Example revenue
//
//			// Save the parking lot into the database
//			parkingLotRepository.save(parkingLot);
//
//			System.out.println("Dummy ParkingLot saved!");
//		};
//	}

}
