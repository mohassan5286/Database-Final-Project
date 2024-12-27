package com.backend.service;

import com.backend.dto.ReservationStatisticsDTO;
import com.backend.repository.ReservationRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ReportService {

    @Autowired
    private ReservationRepository reservationRepository;

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
}
