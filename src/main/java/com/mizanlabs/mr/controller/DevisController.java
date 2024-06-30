package com.mizanlabs.mr.controller;
import com.mizanlabs.mr.entities.*;
import com.mizanlabs.mr.repository.DevisRepository;
import com.mizanlabs.mr.repository.TaskRepository;
import com.mizanlabs.mr.service.LigneDevisService;
//import com.itextpdf.htmlpdf.HtmlConverter;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import jakarta.validation.Valid;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import com.mizanlabs.mr.exceptions.ResourceNotFoundException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import com.mizanlabs.mr.service.ProjectService;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import com.mizanlabs.mr.service.DevisService;
import com.mizanlabs.mr.service.ElementDevisService;
import com.mizanlabs.mr.service.FactureService;
import com.mizanlabs.mr.service.TaskService;

import jakarta.servlet.http.HttpServletResponse;
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

@RestController
@RequestMapping("/api/devis")
public class DevisController {

    private final DevisService devisService;
    private final TaskService taskService;
    private final ElementDevisService elementDevisService;
    private final LigneDevisService ligneDevisService;
    private final ProjectService projectService;
    private final TaskRepository taskRepository;
    private DevisRepository devisRepository;
    private FactureService factureService;

    @Autowired
    public DevisController(DevisRepository devisRepository,DevisService devisService, @Lazy TaskService taskService, ElementDevisService elementDevisService, LigneDevisService ligneDevisService, ProjectService projectService, TaskRepository taskRepository) {
        this.devisService = devisService;
        this.taskService = taskService;
        this.elementDevisService = elementDevisService;
        this.ligneDevisService = ligneDevisService;
        this.projectService = projectService;
        this.devisRepository = devisRepository;


        this.taskRepository = taskRepository;
    }

    @GetMapping("/details")
    public ResponseEntity<ProjectClientDTO> getProjectAndClientNames(@RequestParam Long devisId) {
        Devis devis = devisRepository.findById(devisId)
                .orElseThrow(() -> new ResourceNotFoundException("Devis not found"));
        Project project = devis.getProject();
        Client client = project.getClient();

        ProjectClientDTO projectClientDTO = new ProjectClientDTO();
        projectClientDTO.setProjectName(project.getTitle());
        projectClientDTO.setClientName(client.getName());

        return ResponseEntity.ok(projectClientDTO);
    }

    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @PostMapping()
    public ResponseEntity<?> createDevis(@Valid @RequestBody Devis devis, @RequestParam("projectid") Long projectId) {
        try {
        String refDevis = devisService.generateDevisReference(devis.getAnnee());
        devis.setRef_devis(refDevis);

        // Fetch and set the project for the devis
        Project project = devisService.getProjectById(projectId);
        devis.setProject(project);

        Devis savedDevis = devisService.createDevis(devis);
        return ResponseEntity.ok(savedDevis);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Erreur lors de l'ajout de la devis");
        }
    }

    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @GetMapping("/devis/{devisId}/hastache")
    public boolean hastache(@PathVariable Long devisId) {
        // Rechercher un tache avec l'ID de la devis spécifiée
        Devis devis = new Devis();
        devis.setDevisId(devisId);

        // Vérifier si un élément de devis existe pour cette tâche
        return !taskRepository.findByDevisId(devisId).isEmpty();
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    @GetMapping
    public List<Devis> getAllDevis() {
        return devisService.getAllDevis();
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    @GetMapping("/{id}")
    public ResponseEntity<Devis> getDevisById(@PathVariable Long id) {
        return devisService.getDevisById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @GetMapping("/project/{projectId}")
    public List<Devis> getDevisByProjectId(@PathVariable Long projectId) {
        return devisService.getDevisByProjectId(projectId);
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    @PutMapping("/{id}")
    public ResponseEntity<Devis> updateDevis(@PathVariable Long id, @RequestBody Devis devis) {
        return devisService.updateDevis(id, devis)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

//    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

//    @PutMapping("/updateAmounts/{id}")
//    public ResponseEntity<Devis> updateDeviss(@PathVariable Long id, @RequestBody Devis devis) {
//        return devisService.updateDevismount(id, devis)
//                .map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.notFound().build());
//    }


    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevis(@PathVariable Long id) {
        if (devisService.deleteDevis(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    @PostMapping("/creer_devis")
    public void creerDevis(@RequestBody Project project) {
        // Enregistrer le devis
        Devis devis = new Devis();
        devis.setProject(project); // Projet associé au devis
//        devis.setCreationDate(new Date()); // Date du devis
        devisService.createDevis(devis);

        Integer somme = 0;
        List<Task> tasks = taskService.getTasksByProject(project);

        for (Task task : tasks) {
            List<ElementDevis> elements = elementDevisService.getElementDevisByTask(task);
            for (ElementDevis element : elements) {
                LigneDevis ligneDevis = new LigneDevis();
                ligneDevis.setElementNote(element.getElementNote());
                ligneDevis.setElementQty(element.getElementQty());
                ligneDevis.setStatus(element.getStatus());
                ligneDevis.setClientName(project.getClient().getName());
                ligneDevis.setItemName(element.getElement().getName());
//                ligneDevis.setMontant(element.getElement().getPritunit()* element.getElementQty()); // Prix de l'élément
//                ligneDevis.setItemPrice(element.getElement().getPritunit()); // Prix de l'élément
                ligneDevis.setItemType(String.valueOf(element.getElement().getType()));
//                ligneDevis.setItemUnit(element.getElement().getUnit());
                ligneDevis.setTaskName(task.getTaskName());
                ligneDevis.setTaskId(task.getTaskId());

                ligneDevis.setDevis(devis); // Définir le devis associé à cette ligne de devis

                somme += ligneDevis.getMontant();
                ligneDevisService.createLigneDevis(ligneDevis);
            }
        }

        // Mettre à jour le montant total du devis
        devisService.updateDevis(devis.getDevisId(), devis);
    }

//    @GetMapping("/project/{projectId}")
//    public List<Devis> getDevisByProject(@PathVariable Long projectId) {
//        Project project = projectService.findById(projectId);
//        return devisService.getDevisByProject(project);
//    }




    @GetMapping("/maxRefDevis/{year}")
    public ResponseEntity<String> getMaxRefDevisByYear(@PathVariable String year) {
        String maxRefDevis =devisService.findMaxRefDevisByYear(year);
        return ResponseEntity.ok(maxRefDevis);
    }
    @GetMapping("/generate-reference/{annee}")
    public String generateProjectReference(@PathVariable String  annee) {
        String generatedReference = devisService.generateDevisReference(annee);
        return generatedReference;
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @PostMapping(value = "/pdf/generate", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> generatePDF(@RequestBody Map<String, String> payload, HttpServletResponse response) {
        // HTML content with dynamic image
        String imageTag = "<img src=\"http://localhost:8082/assets/logo.png\" style=\"width: 150px; margin-bottom: 30px; margin-left: 10px;\" alt=\"\" />";
        String htmlContent = "<div>" + imageTag + payload.get("htmlContent") + "</div>";

        try (ByteArrayOutputStream initialPdfOutStream = new ByteArrayOutputStream();
             ByteArrayOutputStream finalPdfOutStream = new ByteArrayOutputStream()) {

            PdfRendererBuilder builder = new PdfRendererBuilder();

            builder.useFastMode();
            // Update this path to your resources path
            builder.withHtmlContent(htmlContent, "file:///C:/path/to/resources/static/assets/");
            builder.toStream(initialPdfOutStream);
            builder.run();

            byte[] initialPdfBytes = initialPdfOutStream.toByteArray();
            try (PDDocument document = PDDocument.load(new ByteArrayInputStream(initialPdfBytes))) {
                PDFont font = PDType1Font.HELVETICA_BOLD;
                float fontSize = 8;
                float margin = 50; // Side margins
                float bottomMargin = 20; // Bottom margin for footer

                int pageNum = 0;
                for (PDPage page : document.getPages()) {
                    pageNum++;
                    PDRectangle pageSize = page.getMediaBox();
                    float width = pageSize.getWidth();
                    float startY = pageSize.getLowerLeftY() + bottomMargin;

                    try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true)) {
                        // Draw line
                        contentStream.setLineWidth(1);
                        contentStream.moveTo(margin, startY - 15);
                        contentStream.lineTo(width - margin, startY - 15);
                        contentStream.stroke();

                        // Footer text
                        contentStream.beginText();
                        contentStream.setFont(font, fontSize);
                        contentStream.newLineAtOffset(margin, startY);
                        contentStream.showText("Groupe ACCESS ENGINEERING/MIZAN LABS, MD B N°252, Tevragh Zeina - Nouakchott");
                        contentStream.newLineAtOffset(0, -fontSize * 1.2f); // Move to next line

                        contentStream.showText("Tél: (P) +222 32 04 66 24, +222 45 29 85 04, Email: bureau.access@gmail.com, siteweb: www.acccess.mr");
                        contentStream.endText();

                        // Page number
                        contentStream.beginText();
                        contentStream.setFont(font, fontSize);
                        contentStream.newLineAtOffset(width - margin - 40, startY);
                        contentStream.showText("Page " + pageNum);
                        contentStream.endText();
                    }
                }

                document.save(finalPdfOutStream);
            }

            byte[] finalPdfBytes = finalPdfOutStream.toByteArray();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "devis.pdf");

            return new ResponseEntity<>(finalPdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating PDF: " + e.getMessage());
        }
    }
    @PostMapping("/{devisId}/copy")
    public ResponseEntity <Devis> copier (@PathVariable long  devisId){
        Devis copie = devisService.copierDevis(devisId);
        Devis savedDevis = devisService.createDevis(copie);

        return ResponseEntity.ok(savedDevis);
    }


    @PostMapping("/{devisId}/coller/{projectId}")
    public ResponseEntity<Object> collerDevis (@PathVariable long  devisId,@PathVariable long  projectId){
        ResponseEntity<Object> copie = devisService.collerDevis(devisId,projectId);
        return copie ;
    }


    @GetMapping("/{devisId}/copyTasks")
    public ResponseEntity <Set<Task>> copyTasks(@PathVariable Long devisId) {
        try {
            Set<Task> copiedTasks = devisService.copier_tache(devisId);
            return ResponseEntity.ok(copiedTasks);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }}

    @PostMapping("/{devisId}/pasteTasks")
    public ResponseEntity <Set<Task>> coller_tache(@PathVariable Long devisId,@RequestBody Set<Task> copiedTasks) {
        try {
//Set<Task> pasteTasks = devisService.copier_tache(devisId);
            return ResponseEntity.ok(copiedTasks);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    @GetMapping("/{devisId}/total-tasks")
    public Integer getTotalTasksByDevisId(@PathVariable Long devisId) {
        return devisService.getTotalTasksByDevisId(devisId);
    }

//    @PutMapping("/{devisId}/update-montant")
//    public void updateDevisMontant(@PathVariable Long devisId) {
//        devisService.updateDevisMontant(devisId);
//    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @PutMapping("/{devisId}/update-montant")
    public void updateDevisMontant(@PathVariable Long devisId, @RequestBody Integer taskAmount) {
        devisService.updateDevisMontant(devisId, taskAmount);
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @PutMapping("/{devisId}/update-montant1")
    public void updateDevisMontant1(@PathVariable Long devisId, @RequestBody Integer taskAmount) {
        devisService.updateDevisMontant1(devisId, taskAmount);
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @GetMapping("/detailsss")
    public DeviProjectClientrefDTO getDevisDetails(@RequestParam("devisId") Long devisId) {
        return devisService.getDevisDetails(devisId);
    }
    @GetMapping("/status-distribution")
    public Map<String, Long> getDevisStatusDistribution() {
        return devisService.getDevisStatusDistribution();
    }
    
    @PutMapping("/valider/{devisId}")
    public ResponseEntity<List<Facture>> validerDevisEtGenererFactures(@PathVariable Long devisId) {
        try {
            List<Facture> factures = factureService.validerDevisEtGenererFactures(devisId);
            return ResponseEntity.ok(factures);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(null);
        }
    }
}