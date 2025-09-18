package com.backend.controller;

import com.backend.service.ReportService;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpHeaders;

@RestController
@CrossOrigin
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/get/occupancyRates")
    public String getAdminDashboardReport() throws Exception {
        System.out.println(1231);
        return reportService.generateAdminReport();
    }
    // API to generate the report for Top Users
    @GetMapping("/get/topUsersReport")
    public String getTopUsersReport() throws Exception {
        System.out.println("Generating Top Users Report...");
        return reportService.generateTopUserReport();
    }

    // API to generate the report for Top Parking Lots
    @GetMapping("/get/topParkingLotsReport")
    public String getTopParkingLotsReport() throws Exception {
        System.out.println("Generating Top Parking Lots Report...");
        return reportService.generateTopParkingSpotReport();
    }
}
