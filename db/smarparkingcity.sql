-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema smartparkingcity
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema smartparkingcity
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `smartparkingcity` DEFAULT CHARACTER SET utf8 ;
USE `smartparkingcity` ;

-- -----------------------------------------------------
-- Table `smartparkingcity`.`Admin`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `smartparkingcity`.`Admin` (
  `AdminID` INT NOT NULL,
  `Name` VARCHAR(45) NOT NULL,
  `Email` VARCHAR(45) NOT NULL,
  `Password` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`AdminID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `smartparkingcity`.`User`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `smartparkingcity`.`User` (
  `idUser` INT NOT NULL,
  `Debt` INT NULL,
  `LicensePlate` VARCHAR(45) NOT NULL,
  `Name` VARCHAR(45) NOT NULL,
  `Password` VARCHAR(45) NOT NULL,
  `PaymentMethod` VARCHAR(45) NOT NULL,
  `Admin_AdminID` INT NOT NULL,
  `Email` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idUser`, `Admin_AdminID`),
  INDEX `fk_User_Admin_idx` (`Admin_AdminID` ASC) VISIBLE,
  CONSTRAINT `fk_User_Admin`
    FOREIGN KEY (`Admin_AdminID`)
    REFERENCES `smartparkingcity`.`Admin` (`AdminID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `smartparkingcity`.`ParkingLot`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `smartparkingcity`.`ParkingLot` (
  `LotID` INT NOT NULL,
  `Directions` VARCHAR(45) NOT NULL,
  `Location` VARCHAR(45) NOT NULL,
  `Capacity` VARCHAR(45) NOT NULL,
  `Admin_AdminID` INT NOT NULL,
  `Revenue` INT NOT NULL,
  PRIMARY KEY (`LotID`, `Admin_AdminID`),
  INDEX `fk_ParkingLot_Admin1_idx` (`Admin_AdminID` ASC) VISIBLE,
  CONSTRAINT `fk_ParkingLot_Admin1`
    FOREIGN KEY (`Admin_AdminID`)
    REFERENCES `smartparkingcity`.`Admin` (`AdminID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `smartparkingcity`.`ParkingSpot`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `smartparkingcity`.`ParkingSpot` (
  `SpotID` INT NOT NULL,
  `Type` VARCHAR(45) NOT NULL,
  `Status` VARCHAR(45) NOT NULL,
  `Price` INT NOT NULL,
  `ParkingLot_LotID` INT NOT NULL,
  `ParkingLot_Admin_AdminID` INT NOT NULL,
  PRIMARY KEY (`SpotID`, `ParkingLot_LotID`, `ParkingLot_Admin_AdminID`),
  INDEX `fk_ParkingSpot_ParkingLot1_idx` (`ParkingLot_LotID` ASC, `ParkingLot_Admin_AdminID` ASC) VISIBLE,
  CONSTRAINT `fk_ParkingSpot_ParkingLot1`
    FOREIGN KEY (`ParkingLot_LotID` , `ParkingLot_Admin_AdminID`)
    REFERENCES `smartparkingcity`.`ParkingLot` (`LotID` , `Admin_AdminID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `smartparkingcity`.`Reservation`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `smartparkingcity`.`Reservation` (
  `ReservationID` INT NOT NULL,
  `Arrived` VARCHAR(45) NOT NULL,
  `StartTime` VARCHAR(45) NOT NULL,
  `EndTime` VARCHAR(45) NOT NULL,
  `User_idUser` INT NOT NULL,
  `User_Admin_AdminID` INT NOT NULL,
  `ParkingSpot_SpotID` INT NOT NULL,
  `ParkingSpot_ParkingLot_LotID` INT NOT NULL,
  `ParkingSpot_ParkingLot_Admin_AdminID` INT NOT NULL,
  PRIMARY KEY (`ReservationID`, `User_idUser`, `User_Admin_AdminID`, `ParkingSpot_SpotID`, `ParkingSpot_ParkingLot_LotID`, `ParkingSpot_ParkingLot_Admin_AdminID`),
  INDEX `fk_Reservation_User1_idx` (`User_idUser` ASC, `User_Admin_AdminID` ASC) VISIBLE,
  INDEX `fk_Reservation_ParkingSpot1_idx` (`ParkingSpot_SpotID` ASC, `ParkingSpot_ParkingLot_LotID` ASC, `ParkingSpot_ParkingLot_Admin_AdminID` ASC) VISIBLE,
  CONSTRAINT `fk_Reservation_User1`
    FOREIGN KEY (`User_idUser` , `User_Admin_AdminID`)
    REFERENCES `smartparkingcity`.`User` (`idUser` , `Admin_AdminID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Reservation_ParkingSpot1`
    FOREIGN KEY (`ParkingSpot_SpotID` , `ParkingSpot_ParkingLot_LotID` , `ParkingSpot_ParkingLot_Admin_AdminID`)
    REFERENCES `smartparkingcity`.`ParkingSpot` (`SpotID` , `ParkingLot_LotID` , `ParkingLot_Admin_AdminID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
