package com.backend.repository;

import com.backend.entity.Admin;
import com.backend.entity.ParkingLot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
}
