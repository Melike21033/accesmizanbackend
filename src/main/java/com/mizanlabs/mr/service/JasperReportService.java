package com.mizanlabs.mr.service;

import com.mizanlabs.mr.entities.Devis;
import com.mizanlabs.mr.entities.ElementDevis;
import com.mizanlabs.mr.entities.Task;
import com.mizanlabs.mr.entities.Type;
import com.mizanlabs.mr.repository.DevisRepository;
import jakarta.persistence.EntityNotFoundException;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JasperReportService {

    private static final String LOGO_PATH = "/jasper/logo.png";

    @Autowired
    private DevisRepository devisRepository;

    @Autowired
    private TaskService taskService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private ElementDevisService elementDevisService;

    public ResponseEntity<byte[]> generateDevisReport(Long devisId) {
        final Logger logger = LoggerFactory.getLogger(JasperReportService.class);
// Change the logo path to be retrieved as a URL.
        URL logoURL = getClass().getResource(LOGO_PATH);
        if (logoURL == null) {
            throw new RuntimeException("Cannot find logo file: " + LOGO_PATH);
        }
        try {
            logger.debug("Fetching devis with ID: {}", devisId);

            Devis devis = devisRepository.findById(devisId)
                    .orElseThrow(() -> new EntityNotFoundException("Devis not found for this id :: " + devisId));
            logger.debug("Fetched devis: {}", devis);
            List<Task> tasks = taskService.getTasksByDevisId(devisId);
            logger.debug("Fetched tasks for devis ID {}: {}", devisId, tasks);
            StringBuilder concatenatedTasks = new StringBuilder();
            for (Task task : tasks) {
                concatenatedTasks.append(task.getTaskName()).append(" - ");
            }
            String concatenatedTasksString = concatenatedTasks.length() > 3 ? concatenatedTasks.substring(0, concatenatedTasks.length() - 3) : "";
            logger.debug("Concatenated tasks string: {}", concatenatedTasksString);
            Map<String, Object> parameters = new HashMap<>();

            parameters.put("DevisID", devis.getDevisId());
            parameters.put("Date", devis.getCreationDate());
            parameters.put("Client", devis.getProject().getClient().getName());
            parameters.put("ProjectName", devis.getProject().getTitle());
            parameters.put("LOGO_PATH", logoURL.toString());
            parameters.put("ConcatenatedTasks", concatenatedTasksString);
            JRBeanCollectionDataSource tasksDataSource = new JRBeanCollectionDataSource(tasks);
            parameters.put("TasksDataSource", tasksDataSource);

            List<ElementDevis> elementDevisList = new ArrayList<>();
            tasks.forEach(task -> elementDevisList.addAll(task.getElementDevis()));
            JRBeanCollectionDataSource elementsDataSource = new JRBeanCollectionDataSource(elementDevisList);
            parameters.put("ElementsDataSource", elementsDataSource);

//            parameters.put("taskname",taskService.getTasksByDevisId(devisId));
//            parameters.put("elementdevis",elementDevisService.getElementDevisByTask((Task) tasks));
            List<Map<String, Object>> tableLines = new ArrayList<>();

            for (Task task : tasks) {
                List<ElementDevis> elements = elementDevisService.getElementDevisByTaskId(task.getTaskId());
                logger.debug("Fetched elements for task ID {}: {}", task.getTaskId(), elements);
                for (ElementDevis element : elements) {
                    Map<String, Object> line = new HashMap<>();
                    line.put("TaskName", task.getTaskName());
                    line.put("ElementName", element.getName());
                    line.put("TypeName",element.getType());
                    line.put("Unité",element.getUnite());
                    line.put("Qte",element.getqteLots());
                    line.put("PrixUnit",element.getPrix_unitaire());
                    // Assurez-vous que PrixUnit est un nombre. Si c'est une chaîne, convertissez-la en Double.
                    double Qte;
                    try {
                        Qte = Double.parseDouble(element.getqteLots());
                    } catch (NumberFormatException e) {
                        // Gérer l'exception si la conversion échoue
                        Qte = 0.0; // Ou toute autre valeur par défaut/logique d'erreur
                    }

                    // Assurez-vous que Qte est un nombre. Si c'est un Integer, convertissez-le en Double pour la multiplication.
                    double prixUnit = element.getPrix_unitaire().doubleValue();

                    // Calculer le Prix Total ici
                    double prixTotal = Qte * prixUnit;
                    line.put("PrixTot", prixTotal);
                    //
                    // Ajouter d'autres attributs comme Unité, Qte, Prix. Unit, etc.
                    tableLines.add(line);
                }
            }
            parameters.put("TableData", new JRBeanCollectionDataSource(tableLines));
            // Pour chaque tâche, ajouter ses éléments de devis
            for (Task task : tasks) {
                List<ElementDevis> elements = elementDevisService.getElementDevisByTaskId(task.getTaskId());
                parameters.put("Elements_Task_" + task.getTaskId(), elements);

                // Pour chaque élément de devis, ajouter les types associés
                for (ElementDevis element : elements) {
                    List<Type> types = elementDevisService.getTypeByElementDevisId(element.getId());
                    parameters.put("Types_Element_" + element.getId(), types);
                }
            }

            double totalHT = 0;
            for (Task task : tasks) {
                double taskTotal = taskService.calculateTaskTotal(task.getTaskId());
                parameters.put("TaskTotal_" + task.getTaskName(), taskTotal);
                totalHT += taskTotal;
            }

            parameters.put("TotalHT", totalHT);
            double remise = (devis.getDiscount() != null ? devis.getDiscount() : 0)
                    + (devis.getDiscountp() != null ? totalHT * devis.getDiscountp() / 100 : 0);
            parameters.put("Remise", remise);
            double totalTTC = totalHT - remise;

            if (devis.getTva_present() != null && devis.getTva_present()) {
                double tva = totalHT * devis.getTVA() / 100;
                totalTTC += tva;
                parameters.put("TVA", tva);
            }

            parameters.put("TotalTTC", totalTTC);
            parameters.put("TotalAPayer", totalTTC);

            InputStream inputStream = getClass().getResourceAsStream("/jasper/Devifinal.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
            byte[] pdfContent = outputStream.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            String filename = "devis_" + devisId + ".pdf";
            headers.setContentDispositionFormData(filename, filename);

            return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
        }catch (JRException e) {
            logger.error("An error occurred while generating the report: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

