package com.backend.service;

import com.backend.dto.ReservationStatisticsDTO;
import com.backend.dto.TopParkingSpotDTO;
import com.backend.dto.TopUserDTO;
import com.backend.entity.User;
import com.backend.repository.ParkingSpotRepository;
import com.backend.repository.ReservationRepository;
import com.backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@AllArgsConstructor
public class ReportService {

    @Autowired
    private ReservationRepository reservationRepository;
    private UserRepository userRepository;
    private ParkingSpotRepository parkingSpotRepository;

    public String generateAdminReport() throws Exception {
        // Fetch report data as List<Object[]>
        List<ReservationStatisticsDTO> data = reservationRepository.getStatisticsForAllLots();

        data.add(new ReservationStatisticsDTO(1, 1500L, 1500.0, 3L));
        data.add(new ReservationStatisticsDTO(2, 2000L, 2000.0, 5L));
        data.add(new ReservationStatisticsDTO(3, 1200L, 1200.0, 2L));
        System.out.println(data.get(0).getLotID());
        // Prepare parameters for the report
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("Date", new Date());

        // Load and compile the report template from classpath
        File template = new File("D:\\Life\\collage\\collage_labs\\year_3\\term_1\\db\\Database-Final-Project\\backend\\src\\main\\resources\\adminDashboard.jrxml");
        if (template == null) {
            throw new RuntimeException("Report template not found");
        }

        JasperReport jasperReport = JasperCompileManager.compileReport(template.getAbsolutePath());

        // Create a data source for the report from List<Object[]>
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data);

        // Fill the report with data
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        // Define the current date and format it as YYYY-MM-DD
        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        // Ensure the reports directory exists
        File reportsDir = new File("reports");
        if (!reportsDir.exists()) {
            reportsDir.mkdir();
        }

        // Define the output path for the report (new file in the reports directory)
        String reportOutputPath = "reports/adminDashboardReport_" + currentDate + ".pdf";

        // Export the report to PDF
        JasperExportManager.exportReportToPdfFile(jasperPrint, reportOutputPath);

        return reportOutputPath; // Return the file path where the report is saved
    }
    public String generateTopUserReport() throws Exception {
        // Fetch report data for top users
        List<TopUserDTO> topUsers = userRepository.getTopUsers();

        topUsers.add(new TopUserDTO(1, 101, 15L)); // User 1 with admin 101 and 15 reservations
        topUsers.add(new TopUserDTO(2, 101, 12L)); // User 2 with admin 101 and 12 reservations
        topUsers.add(new TopUserDTO(3, 102, 10L)); // User 3 with admin 102 and 10 reservations
        topUsers.add(new TopUserDTO(4, 102, 8L));  // User 4 with admin 102 and 8 reservations
        topUsers.add(new TopUserDTO(5, 103, 6L));

        // Prepare parameters for the report
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("Date", new Date());

        // Load and compile the report template from classpath
        File template = new File("D:\\Life\\collage\\collage_labs\\year_3\\term_1\\db\\Database-Final-Project\\backend\\src\\main\\resources\\topUsers.jrxml");
        if (template == null) {
            throw new RuntimeException("Report template not found");
        }

        JasperReport jasperReport = JasperCompileManager.compileReport(template.getAbsolutePath());

        // Create a data source for the report from List<TopUserDTO>
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(topUsers);

        // Fill the report with data
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        // Define the current date and format it as YYYY-MM-DD
        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        // Ensure the reports directory exists
        File reportsDir = new File("reports");
        if (!reportsDir.exists()) {
            reportsDir.mkdir();
        }

        // Define the output path for the report (new file in the reports directory)
        String reportOutputPath = "reports/topUserReport_" + currentDate + ".pdf";

        // Export the report to PDF
        JasperExportManager.exportReportToPdfFile(jasperPrint, reportOutputPath);

        return reportOutputPath; // Return the file path where the report is saved
    }
    public String generateTopParkingSpotReport() throws Exception {
        // Fetch report data for top parking spots
        List<TopParkingSpotDTO> topParkingSpots = parkingSpotRepository.getTopParkingLots();

        topParkingSpots.add(new TopParkingSpotDTO(1, 150.0));
        topParkingSpots.add(new TopParkingSpotDTO(2, 120.0));
        topParkingSpots.add(new TopParkingSpotDTO(3, 100.0));
        topParkingSpots.add(new TopParkingSpotDTO(4, 90.0));
        topParkingSpots.add(new TopParkingSpotDTO(5, 75.0));


        // Prepare parameters for the report
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("Date", new Date());

        // Load and compile the report template from classpath
        File template = new File("D:\\Life\\collage\\collage_labs\\year_3\\term_1\\db\\Database-Final-Project\\backend\\src\\main\\resources\\topParkingSpots.jrxml");
        if (template == null) {
            throw new RuntimeException("Report template not found");
        }

        JasperReport jasperReport = JasperCompileManager.compileReport(template.getAbsolutePath());

        // Create a data source for the report from List<TopParkingLotDTO>
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(topParkingSpots);

        // Fill the report with data
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        // Define the current date and format it as YYYY-MM-DD
        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        // Ensure the reports directory exists
        File reportsDir = new File("reports");
        if (!reportsDir.exists()) {
            reportsDir.mkdir();
        }

        // Define the output path for the report (new file in the reports directory)
        String reportOutputPath = "reports/topParkingSpotReport_" + currentDate + ".pdf";

        // Export the report to PDF
        JasperExportManager.exportReportToPdfFile(jasperPrint, reportOutputPath);

        return reportOutputPath; // Return the file path where the report is saved
    }


}
