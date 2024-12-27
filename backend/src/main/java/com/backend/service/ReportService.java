package com.backend.service;


import com.backend.entity.ReservationStatisticsDTO;
import com.backend.repository.ReservationRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.File;

@Service
public class ReportService {

    @Autowired
    private ReservationRepository reservationRepository;



    public String generateAdminReport() throws Exception {
        // Fetch report data
        List<ReservationStatisticsDTO> data = reservationRepository.getStatisticsForAllLots();

        // Prepare parameters for the report
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("Date", new Date());

        // Load and compile the report template
        InputStream template = this.getClass().getResourceAsStream("classpath:admin_dashboard.jrxml"); // Use absolute path for the template
        JasperReport jasperReport = JasperCompileManager.compileReport(template);

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
