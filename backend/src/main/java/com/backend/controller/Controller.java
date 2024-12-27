package com.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin
@RestController
public class Controller {

    private static final Map<Long, Map<String, String>> mockUsers = new HashMap<>();

    static {
        mockUsers.put(1L, Map.of(
                "name", "John Doe",
                "email", "john.doe@example.com",
                "plateNumber", "ABC123"
        ));
        mockUsers.put(2L, Map.of(
                "name", "Jane Smith",
                "email", "jane.smith@example.com",
                "plateNumber", "DEF456"
        ));
        mockUsers.put(3L, Map.of(
                "name", "Alice Jones",
                "email", "alice.jones@example.com",
                "plateNumber", "GHI789"
        ));
    }

    private List<Map<String, Object>> parkingLots = List.of(
            Map.of(
                    "lot_id", 1L,
                    "location", "123 Main St",
                    "admin_id", 1L,
                    "directions", "Enter the parking lot from the main entrance, take the first left, and the spots will be on your right.",
                    "spots", List.of(
                            new HashMap<>(Map.of("spot_id", 101L, "spot_type", "Regular", "status", "Available", "user_id", "")),
                            new HashMap<>(Map.of("spot_id", 102L, "spot_type", "Disabled", "status", "Reserved", "user_id", 1L)),
                            new HashMap<>(Map.of("spot_id", 103L, "spot_type", "EV Charging", "status", "Occupied", "user_id", 2L))
                    )
            ),
            Map.of(
                    "lot_id", 2L,
                    "location", "456 Elm St",
                    "admin_id", 2L,
                    "directions", "Enter the parking lot from the main entrance, take the first left, and the spots will be on your right.",
                    "spots", List.of(
                            new HashMap<>(Map.of("spot_id", 201L, "spot_type", "Regular", "status", "Occupied", "user_id", 3L)),
                            new HashMap<>(Map.of("spot_id", 202L, "spot_type", "Regular", "status", "Available", "user_id", "")),
                            new HashMap<>(Map.of("spot_id", 203L, "spot_type", "EV Charging", "status", "Reserved", "user_id", 4L))
                    )
            ),
            Map.of(
                    "lot_id", 3L,
                    "location", "789 Oak St",
                    "admin_id", 3L,
                    "directions", "Enter the parking lot from the main entrance, take the first left, and the spots will be on your right.",
                    "spots", List.of(
                            new HashMap<>(Map.of("spot_id", 301L, "spot_type", "Regular", "status", "Available", "user_id", "")),
                            new HashMap<>(Map.of("spot_id", 302L, "spot_type", "Disabled", "status", "Occupied", "user_id", 5L)),
                            new HashMap<>(Map.of("spot_id", 303L, "spot_type", "EV Charging", "status", "Available", "user_id", ""))
                    )
            ),
            Map.of(
                    "lot_id", 4L,
                    "location", "101 Pine St",
                    "admin_id", 4L,
                    "directions", "Enter the parking lot from the main entrance, take the first left, and the spots will be on your right.",
                    "spots", List.of(
                            new HashMap<>(Map.of("spot_id", 401L, "spot_type", "Regular", "status", "Reserved", "user_id", 6L)),
                            new HashMap<>(Map.of("spot_id", 402L, "spot_type", "Regular", "status", "Occupied", "user_id", 7L)),
                            new HashMap<>(Map.of("spot_id", 403L, "spot_type", "Disabled", "status", "Available", "user_id", ""))
                    )
            ),
            Map.of(
                    "lot_id", 5L,
                    "location", "202 Maple St",
                    "admin_id", 5L,
                    "directions", "Enter the parking lot from the main entrance, take the first left, and the spots will be on your right.",
                    "spots", List.of(
                            new HashMap<>(Map.of("spot_id", 501L, "spot_type", "Regular", "status", "Available", "user_id", "")),
                            new HashMap<>(Map.of("spot_id", 502L, "spot_type", "EV Charging", "status", "Reserved", "user_id", 8L)),
                            new HashMap<>(Map.of("spot_id", 503L, "spot_type", "Disabled", "status", "Occupied", "user_id", 9L))
                    )
            )
    );

    @GetMapping("/getUserInformation/{userId}")
    public ResponseEntity<Map<String, String>> getUserInformation(@PathVariable Long userId) {
        Map<String, String> user = mockUsers.get(userId);
        if (user != null) {
            return ResponseEntity.ok(user); // Return 200 OK with user data
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND) // Return 404 Not Found
                    .body(Map.of("message", "User with ID " + userId + " not found.")); // With message
        }
    }

    @GetMapping("/parkingLots")
    public ResponseEntity<List<Map<String, Object>>> getParkingLots() {
        return ResponseEntity.ok(parkingLots);
    }

    @PostMapping("/reserveSpot")
    public ResponseEntity<?> reserveSpot(@RequestBody Map<String, Object> reservationDetails) {
        try {
            Long spotId = Long.valueOf(reservationDetails.get("ParkingSpot_SpotID").toString());
            Long userId = Long.valueOf(reservationDetails.get("User_idUser").toString());
            Long lotId = Long.valueOf(reservationDetails.get("ParkingSpot_ParkingLot_LotID").toString());
            Long adminId = Long.valueOf(reservationDetails.get("ParkingSpot_ParkingLot_Admin_AdminID").toString());
            String location = reservationDetails.get("location").toString();
            String startTime = reservationDetails.get("StartTime").toString();
            String endTime = reservationDetails.get("EndTime").toString();
            Boolean arrived = Boolean.valueOf(reservationDetails.get("Arrived").toString());

            Optional<Map<String, Object>> optionalSpot = parkingLots.stream()
                    .flatMap(lot -> ((List<Map<String, Object>>) lot.get("spots")).stream())
                    .filter(spot -> spotId.equals(Long.valueOf(spot.get("spot_id").toString())))
                    .findFirst();

            if (optionalSpot.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Spot not found"));
            }

            Map<String, Object> spot = optionalSpot.get();

            if ("Reserved".equals(spot.get("status")) || "Occupied".equals(spot.get("status"))) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "Spot is already reserved or occupied"));
            }

            spot.put("status", "Reserved");
            spot.put("user_id", userId);
            spot.put("start_time", startTime);
            spot.put("end_time", endTime);
            spot.put("lot_id", lotId);
            spot.put("admin_id", adminId);
            spot.put("location", location);
            spot.put("arrived", arrived);

            return ResponseEntity.ok(Map.of("message", "Reservation successful"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Internal Server Error"));
        }
    }

    @PostMapping("/arrive")
    public ResponseEntity<?> arrive(@RequestBody Map<String, Object> arrivalDetails) {
        try {
            Long spotId = Long.valueOf(arrivalDetails.get("spotId").toString());
            Long userId = Long.valueOf(arrivalDetails.get("userId").toString());

            Optional<Map<String, Object>> optionalSpot = parkingLots.stream()
                    .flatMap(lot -> ((List<Map<String, Object>>) lot.get("spots")).stream())
                    .filter(spot -> spotId.equals(Long.valueOf(spot.get("spot_id").toString())))
                    .findFirst();

            if (optionalSpot.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Spot not found"));
            }

            Map<String, Object> spot = optionalSpot.get();

            if (!"Reserved".equals(spot.get("status")) || !userId.equals(Long.valueOf(spot.get("user_id").toString()))) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "Spot is not reserved by this user or already occupied"));
            }

            spot.put("status", "Occupied");

            return ResponseEntity.ok(Map.of("message", "Arrived successfully"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Internal Server Error"));
        }
    }
}