package com.backend;

import com.backend.entity.Reservation;
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
//			reservation.setUserId(101); // Foreign key to User
//			reservation.setParkingSpotId(15); // Foreign key to ParkingSpot
//
//			// Save the reservation into the database
//			reservationRepository.save(reservation);
//
//			System.out.println("Dummy Reservation saved!");
//		};
//	};


}
