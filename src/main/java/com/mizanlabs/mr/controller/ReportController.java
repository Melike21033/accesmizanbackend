package com.mizanlabs.mr.controller;

import com.mizanlabs.mr.service.ReportService;
import net.sf.jasperreports.engine.JRException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RequestMapping("/api/devis")
@RestController
public class ReportController {

    private final ReportService reportService;
    private static final Logger logger = LoggerFactory.getLogger(ReportController.class);

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/reports/{devisId}")
    public ResponseEntity<byte[]> exportDevisReport(@PathVariable Long devisId) {
        logger.info("Starting exportDevisReport for devisId: {}", devisId);
        try {
            byte[] reportBytes = reportService.exportDevisReport(devisId);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "devifinal.pdf");
            headers.setContentLength(reportBytes.length);

            logger.info("Report generated successfully for devisId: {}", devisId);
            return new ResponseEntity<>(reportBytes, headers, HttpStatus.OK);
        } catch (JRException e) {
            logger.error("JRException occurred while generating report for devisId: {}", devisId, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (SQLException e) {
            logger.error("SQLException occurred while generating report for devisId: {}", devisId, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }  catch (Exception e) {
            logger.error("Unexpected exception occurred while generating report for devisId: {}", devisId, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
