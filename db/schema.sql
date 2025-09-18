-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema smartparkingcityparking_spot
-- -----------------------------------------------------
SET FOREIGN_KEY_CHECKS = 0;
reservationCREATE SCHEMA IF NOT EXISTS `smartparkingcity` DEFAULT CHARACTER SET utf8 ;
USE `smartparkingcity` ;

-- -----------------------------------------------------
-- Schema `smartparkingcity`
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `smartparkingcity` DEFAULT CHARACTER SET utf8;
USE `smartparkingcity`;

-- -----------------------------------------------------
-- Table `user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `user` (
  `id_user` INT NOT NULL AUTO_INCREMENT,
  `admin_adminid` INT NOT NULL DEFAULT 0,
  `debt` INT DEFAULT 0,
  `email` VARCHAR(255) NOT NULL DEFAULT '',
  `license_plate` VARCHAR(255) NOT NULL DEFAULT '',
  `name` VARCHAR(255) NOT NULL DEFAULT '',
  `password` VARCHAR(255) NOT NULL DEFAULT '',
  `payment_method` VARCHAR(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`id_user`),
  INDEX `fk_user_admin_idx` (`admin_adminid`),
  CONSTRAINT `fk_user_admin`
    FOREIGN KEY (`admin_adminid`)
    REFERENCES `admin` (`adminid`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE=InnoDB;

-- -----------------------------------------------------
-- Table `admin`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `admin` (
  `adminid` INT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(255) NOT NULL DEFAULT '',
  `name` VARCHAR(255) NOT NULL DEFAULT '',
  `password` VARCHAR(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`adminid`)
) ENGINE=InnoDB;

-- -----------------------------------------------------
-- Table `parking_lot`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `parking_lot` (
  `lotid` INT NOT NULL AUTO_INCREMENT,
  `capacity` INT NOT NULL DEFAULT 0,
  `directions` VARCHAR(255) NOT NULL DEFAULT '',
  `location` VARCHAR(255) NOT NULL DEFAULT '',
  `revenue` INT DEFAULT 0,
  PRIMARY KEY (`lotid`)
) ENGINE=InnoDB;

-- -----------------------------------------------------
-- Table `parking_spot`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `parking_spot` (
  `spotid` INT NOT NULL AUTO_INCREMENT,
  `parking_lot_lotid` INT NOT NULL DEFAULT 0,
  `price` INT NOT NULL DEFAULT 0,
  `status` VARCHAR(45) NOT NULL DEFAULT '',
  `type` VARCHAR(45) NOT NULL DEFAULT '',
  PRIMARY KEY (`spotid`),
  INDEX `fk_parking_spot_parking_lot_idx` (`parking_lot_lotid`),
  CONSTRAINT `fk_parking_spot_parking_lot`
    FOREIGN KEY (`parking_lot_lotid`)
    REFERENCES `parking_lot` (`lotid`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE=InnoDB;

-- -----------------------------------------------------
-- Table `reservation`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `reservation` (
  `reservationid` INT NOT NULL AUTO_INCREMENT,
  `arrived` VARCHAR(45) NOT NULL DEFAULT '',
  `start_time` VARCHAR(45) NOT NULL DEFAULT '',
  `end_time` VARCHAR(45) NOT NULL DEFAULT '',
  `user_id_user` INT NOT NULL DEFAULT 0,
  `parking_spot_spotid` INT NOT NULL DEFAULT 0,
  PRIMARY KEY (`reservationid`),
  INDEX `fk_reservation_user_idx` (`user_id_user`),
  INDEX `fk_reservation_parking_spot_idx` (`parking_spot_spotid`),
  CONSTRAINT `fk_reservation_user`
    FOREIGN KEY (`user_id_user`)
    REFERENCES `user` (`id_user`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_reservation_parking_spot`
    FOREIGN KEY (`parking_spot_spotid`)
    REFERENCES `parking_spot` (`spotid`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE=InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

DELIMITER $$

CREATE EVENT update_spot_status_and_debt
ON SCHEDULE EVERY 1 MINUTE
DO
BEGIN
    DECLARE current_time DATETIME;
    
    -- Set the current time
    SET current_time = NOW();
    
    -- Update parking spot status to 'Available' for expired reservations
    UPDATE parking_spot ps
    INNER JOIN reservation r
    ON ps.spotid = r.parking_spot_spotid
    SET ps.status = 'Available'
    WHERE r.end_time < current_time;

    -- Apply penalty for users who did not arrive
    UPDATE user u
    INNER JOIN reservation r
    ON u.id_user = r.user_id_user
    SET u.debt = 500
    WHERE r.end_time < current_time AND r.arrived = 'No';
END$$

DELIMITER ;

DELIMITER $$

CREATE PROCEDURE UpdateSpotStatusAndDebt()
BEGIN
    -- Update ParkingSpot status to 'Available' for expired reservations
    UPDATE smartparkingcity.ParkingSpot PS
    INNER JOIN smartparkingcity.Reservation R
    ON PS.SpotID = R.ParkingSpot_SpotID
    SET PS.Status = 'Available'
    WHERE STR_TO_DATE(R.EndTime, '%Y-%m-%d %H:%i:%s') < NOW();

    -- Apply penalty for users who did not arrive
    UPDATE smartparkingcity.User U
    INNER JOIN smartparkingcity.Reservation R
    ON U.idUser = R.User_idUser
    SET U.Debt = 500
    WHERE STR_TO_DATE(R.EndTime, '%Y-%m-%d %H:%i:%s') < NOW()
      AND R.Arrived = 'No';
END$$

DELIMITER ;



-- Event to update parking spot status and user debt
CREATE EVENT update_spot_status_and_debt
ON SCHEDULE EVERY 1 MINUTE
DO
BEGIN
    -- Update parking spot status to 'Available' for expired reservations
    UPDATE parking_spot ps
    INNER JOIN reservation r
    ON ps.spotid = r.parking_spot_spotid
    SET ps.status = 'Available'
    WHERE r.end_time < NOW();

    -- Apply penalty for users who did not arrive
    UPDATE user u
    INNER JOIN reservation r
    ON u.id_user = r.user_id_user
    SET u.debt = 500
    WHERE r.end_time < NOW() AND r.arrived = 'No';
END$$

DELIMITER ;


DELIMITER $$

CREATE PROCEDURE ReserveParkingSpot(
    IN p_SpotID INT,
    IN p_UserID INT,
    IN p_StartTime VARCHAR(45),
    IN p_EndTime VARCHAR(45)
)
BEGIN
    DECLARE spotStatus VARCHAR(45);

    -- Start transaction for concurrency control
    START TRANSACTION;

    -- Lock the specific parking spot row to prevent other users from reserving it simultaneously
    SELECT status INTO spotStatus
    FROM parking_spot
    WHERE spotid = p_SpotID
    FOR UPDATE;

    -- Check if the spot is currently available
    IF spotStatus = 'Available' THEN
        -- Insert the reservation into the reservation table
        INSERT INTO reservation (
            arrived, start_time, end_time, 
            user_id_user, parking_spot_spotid
        )
        VALUES (
            'No', p_StartTime, p_EndTime, 
            p_UserID, p_SpotID
        );

        -- Update the parking spot status to 'Reserved'
        -- UPDATE parking_spot
        -- SET status = 'Reserved'
        -- WHERE spotid = p_SpotID;

        -- Commit the transaction if the reservation is successful
        COMMIT;
    ELSE
        -- Rollback the transaction if the spot is not available
        ROLLBACK;
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Parking spot is not available.';
    END IF;
END$$

DELIMITER ;

-- Populate admin table
INSERT INTO admin (adminid, name, email, password)
VALUES
    (1, 'John Doe', 'admin1@parkingsystem.com', 'password123'),
    (2, 'Jane Smith', 'admin2@parkingsystem.com', 'password456'),
    (3, 'Samuel Wilson', 'admin3@parkingsystem.com', 'password789'),
    (4, 'Emily Johnson', 'admin4@parkingsystem.com', 'password321');

-- Populate user table
INSERT INTO user (id_user, debt, license_plate, name, password, payment_method, admin_adminid, email)
VALUES
    (1, 0, 'ABC123', 'Alice Johnson', 'alicepass', 'Credit Card', 1, 'alice@example.com'),
    (2, 20, 'XYZ789', 'Bob Brown', 'bobpass', 'Cash', 1, 'bob@example.com'),
    (3, 10, 'LMN456', 'Charlie Davis', 'charliepass', 'Debit Card', 2, 'charlie@example.com'),
    (4, 0, 'JKL123', 'Diana Prince', 'dianapass', 'Credit Card', 2, 'diana@example.com');

-- Populate parking_lot table
INSERT INTO parking_lot (lotid, directions, location, capacity, admin_adminid, revenue)
VALUES
    (1, 'Turn left after main gate', 'Downtown Garage', 100, 1, 5000),
    (2, 'Next to the mall entrance', 'Mall Parking', 150, 2, 7500);

-- Populate parking_spot table
INSERT INTO parking_spot (spotid, type, status, price, parking_lot_lotid)
VALUES
    (1, 'Regular', 'Available', 10, 1),
    (2, 'EV', 'Reserved', 15, 1);

-- Populate reservation table
INSERT INTO reservation (reservationid, arrived, start_time, end_time, user_id_user, parking_spot_spotid)
VALUES
    (1, 'No', '2024-12-26 09:00:00', '2024-12-26 12:00:00', 1, 2);

-- Disable foreign key checks
SET FOREIGN_KEY_CHECKS = 0;

-- Drop existing tables if they exist
DROP TABLE IF EXISTS reservation;
DROP TABLE IF EXISTS parking_spot;
DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS admin;

-- Create `admin` table
CREATE TABLE admin (
    adminid INT PRIMARY KEY,
    email VARCHAR(255),
    name VARCHAR(255),
    password VARCHAR(255)
);

-- Create `user` table
CREATE TABLE user (
    id_user INT PRIMARY KEY,
    admin_adminid INT,
    debt INT,
    email VARCHAR(255),
    license_plate VARCHAR(255),
    name VARCHAR(255),
    password VARCHAR(255),
    payment_method VARCHAR(255)
);

-- Create `parking_spot` table
CREATE TABLE parking_spot (
    spotid INT AUTO_INCREMENT PRIMARY KEY,
    parking_lot_lotid INT,
    price INT,
    status VARCHAR(45),
    type VARCHAR(45)
);

-- Create `reservation` table
CREATE TABLE reservation (
    reservationid INT AUTO_INCREMENT PRIMARY KEY,
    arrived VARCHAR(45),
    start_time VARCHAR(45),
    end_time VARCHAR(45),
    parking_spot_spotid INT,
    user_id_user INT
);

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;

-- Populate `admin` table
INSERT INTO admin (adminid, email, name, password)
VALUES
    (1, 'admin1@parkingsystem.com', 'John Doe', 'password123'),
    (2, 'admin2@parkingsystem.com', 'Jane Smith', 'password456'),
    (3, 'admin3@parkingsystem.com', 'Samuel Wilson', 'password789'),
    (4, 'admin4@parkingsystem.com', 'Emily Johnson', 'password321'),
    (5, 'admin5@parkingsystem.com', 'Nick Fury', 'shieldpass'),
    (6, 'admin6@parkingsystem.com', 'Maria Hill', 'hill123');

-- Populate `user` table
INSERT INTO user (id_user, admin_adminid, debt, email, license_plate, name, password, payment_method)
VALUES
    (5, 3, 15, 'bruce.wayne@example.com', 'NOP456', 'Bruce Wayne', 'brucepass', 'Mobile Payment'),
    (6, 4, 30, 'clark.kent@example.com', 'QRS789', 'Clark Kent', 'clarkpass', 'Credit Card'),
    (7, 3, 5, 'tony.stark@example.com', 'TUV001', 'Tony Stark', 'ironman123', 'Cash'),
    (8, 2, 0, 'steve.rogers@example.com', 'CAP001', 'Steve Rogers', 'shieldpass', 'Credit Card'),
    (9, 1, 20, 'natasha.romanoff@example.com', 'BLK456', 'Natasha Romanoff', 'widowpass', 'Debit Card'),
    (10, 4, 10, 'peter.parker@example.com', 'SPY123', 'Peter Parker', 'webslinger', 'Mobile Payment'),
    (11, 3, 0, 'bruce.banner@example.com', 'HUL123', 'Bruce Banner', 'smashpass', 'Cash');

-- Populate `parking_spot` table
INSERT INTO parking_spot (spotid, parking_lot_lotid, price, status, type)
VALUES
    (3, 1, 12, 'Available', 'Disabled'),
    (4, 2, 10, 'Available', 'Regular'),
    (5, 2, 20, 'Occupied', 'EV'),
    (6, 3, 8, 'Available', 'Regular'),
    (7, 3, 10, 'Occupied', 'Disabled'),
    (8, 3, 15, 'Reserved', 'EV'),
    (9, 4, 10, 'Available', 'Regular'),
    (10, 4, 12, 'Occupied', 'Regular'),
    (11, 5, 10, 'Available', 'Disabled'),
    (12, 5, 20, 'Available', 'EV'),
    (13, 5, 10, 'Available', 'Regular'),
    (14, 2, 10, 'Occupied', 'Regular'),
    (15, 3, 18, 'Available', 'EV'),
    (16, 3, 15, 'Occupied', 'Regular'),
    (17, 2, 12, 'Reserved', 'Disabled'),
    (18, 1, 10, 'Available', 'Regular'),
    (19, 4, 20, 'Occupied', 'EV'),
    (20, 5, 25, 'Available', 'Premium');

-- Populate `reservation` table
INSERT INTO reservation (reservationid, arrived, start_time, end_time, user_id_user, parking_spot_spotid)
VALUES
    (2, 'Yes', '2024-12-26 08:00:00', '2024-12-26 10:00:00', 2, 5),
    (3, 'No', '2024-12-26 14:00:00', '2024-12-26 16:00:00', 3, 14),
    (4, 'Yes', '2024-12-25 18:00:00', '2024-12-25 20:00:00', 4, 7),
    (5, 'Yes', '2024-12-26 10:00:00', '2024-12-26 13:00:00', 5, 6),
    (6, 'No', '2024-12-26 15:00:00', '2024-12-26 17:00:00', 6, 8),
    (7, 'No', '2024-12-27 09:00:00', '2024-12-27 11:00:00', 7, 15),
    (8, 'Yes', '2024-12-27 10:00:00', '2024-12-27 12:00:00', 8, 16),
    (9, 'No', '2024-12-27 13:00:00', '2024-12-27 15:00:00', 9, 17),
    (10, 'Yes', '2024-12-27 14:00:00', '2024-12-27 16:00:00', 10, 18),
    (11, 'No', '2024-12-27 15:00:00', '2024-12-27 17:00:00', 11, 19);

-- Populate `parking_lot` table
INSERT INTO parking_lot (lotid, capacity, directions, location, revenue, admin_adminid) VALUES

(3, 200, 'Next to the Park', '789 Pine St, Riverside', 12000, 3),
(4, 75, 'Close to the Theater', '321 Maple St, Downtown', 3000, 4),
(5, 50, 'Near the Train Station', '654 Birch St, Midtown', 2500, 1),
(6, 120, 'In front of the Stadium', '987 Cedar St, Northside', 9000, 2),
(7, 80, 'Behind the Library', '135 Willow St, Eastside', 4000, 3),
(8, 200, 'Next to the Hospital', '246 Spruce St, Westside', 15000, 4),
(9, 60, 'In front of the Hotel', '369 Ash St, Lakeside', 3500, 1),
(10, 110, 'Across from the Mall', '159 Fir St, Suburbs', 8000, 2);

