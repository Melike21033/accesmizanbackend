package com.mizanlabs.mr.service;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRFontNotFoundException;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReportService {

    private final DataSource dataSource;

    public ReportService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public byte[] exportDevisReport(Long devisId) throws JRException, SQLException {
        try (Connection connection = dataSource.getConnection()) {
            Map<String, Object> params = new HashMap<>();
            params.put("devis_id", devisId);

            InputStream reportStream = getMainReportFile();
            if (reportStream == null) {
                throw new JRException("Le fichier de rapport Devifinal.jasper n'a pas été trouvé.");
            }

            JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, params, connection);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);

            return outputStream.toByteArray();
        } catch (JRFontNotFoundException e) {
            System.err.println("Font not found: " + e.getMessage());
            throw new JRException("Font not found: " + e.getMessage(), e);
        }
    }

    private InputStream getMainReportFile() {
        InputStream inputStream = getClass().getResourceAsStream("/jasper/Devifinal.jasper");
        if (inputStream == null) {
            throw new RuntimeException("Le fichier de rapport Devifinal.jasper n'a pas été trouvé.");
        }
        return inputStream;
    }
}

