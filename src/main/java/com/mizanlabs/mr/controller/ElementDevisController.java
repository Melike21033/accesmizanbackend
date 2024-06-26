//ElementDevisController
package com.mizanlabs.mr.controller;
import com.mizanlabs.mr.entities.Type; // Assurez-vous d'avoir un service pour récupérer le type par son ID ou autre propriété unique

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mizanlabs.mr.entities.Element;
import com.mizanlabs.mr.entities.ElementDevis;
import com.mizanlabs.mr.entities.Task;
import com.mizanlabs.mr.service.ElementDevisService;
import com.mizanlabs.mr.service.ElementService;
import com.mizanlabs.mr.service.TaskService;
import com.mizanlabs.mr.service.TypeService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/elementdevis")
public class ElementDevisController {

    private final ElementDevisService elementDevisService;
    private final TaskService taskService;
    private final TypeService typeService;
    private final ElementService elementService;

    @Autowired
    public ElementDevisController(ElementDevisService elementDevisService,TaskService taskService,TypeService typeService,ElementService elementService) {
        this.elementDevisService = elementDevisService;
        this.taskService = taskService;
        this.typeService = typeService;
        this.elementService = elementService;



    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @GetMapping
    public ResponseEntity<List<ElementDevis>> getAllElementDevis() {
        List<ElementDevis> elementDevisList = elementDevisService.getAllElementDevis();
        return ResponseEntity.ok(elementDevisList);
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    // Endpoint pour récupérer tous les éléments de devis associés à une tâche spécifique
    @GetMapping("/byTask/{taskId}") // Modification ici
    public ResponseEntity<List<ElementDevis>> getElementDevisByTask(@PathVariable Long taskId) { // Modification ici

        Optional<Task> taskOptional = taskService.getTaskById(taskId); // Modification ici

        if (taskOptional.isPresent()) {
            List<ElementDevis> elementDevisList = elementDevisService.getElementDevisByTask(taskOptional.get());
            return ResponseEntity.ok(elementDevisList);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<ElementDevis>> getElementDevisByTaskId(@PathVariable Long taskId) {
        List<ElementDevis> elementDevisList = elementDevisService.getElementDevisByTaskId(taskId);
        return ResponseEntity.ok(elementDevisList);
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @GetMapping("/{id}")
    public ResponseEntity<ElementDevis> getElementDevisById(@PathVariable Long id) {
        Optional<ElementDevis> elementDevis = elementDevisService.getElementDevisById(id);
        return elementDevis.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @PostMapping
    public ResponseEntity<ElementDevis> addElementDevis(@RequestBody ElementDevis elementDevis) {
        ElementDevis savedElementDevis = elementDevisService.addElementDevis(elementDevis);
        return ResponseEntity.ok(savedElementDevis);
    }

    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @PutMapping("/{id}")
    public ResponseEntity<ElementDevis> updateElementDevis(@PathVariable Long id, @RequestBody ElementDevis elementDevis) {
        return elementDevisService.updateElementDevis(id, elementDevis)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteElementDevis(@PathVariable Long id) {
        if (elementDevisService.deleteElementDevis(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }}
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    @GetMapping("/generateRefEdevis/{taskId}")
    public ResponseEntity<?> generateNextRefEdevisForTask(@PathVariable Long taskId) {
        try {
            String refEdevis = elementDevisService.getMaxRefEdevisForTask(taskId);
            return ResponseEntity.ok(refEdevis);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred: " + e.getMessage());
        }
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @GetMapping("/{id}/refEdevis")
    public ResponseEntity<String> getRefEdevisById(@PathVariable Long id) {
        String refEdevis = elementDevisService.getRefEdevisById(id);
        if (refEdevis != null) {
            return ResponseEntity.ok(refEdevis);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    @PostMapping("/assign-to-task")
    public ResponseEntity<ElementDevis> assignElementDevisToTask(@RequestBody ElementDevis elementDevis, @RequestParam Long taskId, @RequestParam Long elementId) {
        String refEdevis = elementDevisService.getMaxRefEdevisForTask(taskId);
        elementDevis.setRefEdevis(refEdevis);

        Optional<Element> element = elementService.getElementById(elementId);
        if (element.isPresent()) {
            elementDevis.setElement(element.get());
            elementDevis.setName(element.get().getName());

            ElementDevis createdElementDevis = elementDevisService.createAndAssignElementDevisToTask(elementDevis, taskId, elementId);
            return new ResponseEntity<>(createdElementDevis, HttpStatus.CREATED);
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    @GetMapping("/Edevis/{taskName}")
    public ResponseEntity<List<ElementDevis>> getElementDevisByTaskNameAndOptionalType(@PathVariable String taskName,
                                                                                       @RequestParam Optional<Long> typeId) {
        Type type = null;
        if (typeId.isPresent()) {
            // Récupère l'entité Type en utilisant l'identifiant fourni. Vous devrez implémenter la méthode getTypeById dans votre TypeService.
            Optional<Type> typeOpt = typeService.getTypeById(typeId.get());
            if (typeOpt.isPresent()) {
                type = typeOpt.get();
            } else {
                // Gérer le cas où le type avec l'ID fourni n'existe pas
                return ResponseEntity.badRequest().build();
            }
        }
        // Appelle la méthode du service avec le taskName et le type (qui peut être null si typeId n'est pas fourni)
        List<ElementDevis> elementDevis = elementDevisService.getElementDevisByTaskNameAndType(taskName, type);
        return ResponseEntity.ok(elementDevis);
    }
    //test
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    @GetMapping("/by-task/{taskName}")
    public ResponseEntity<List<ElementDevis>> getElementDevisByTaskNameSortedByType(@PathVariable String taskName) {
        List<ElementDevis> elementDevisList = elementDevisService.getElementDevisByTaskNameSortedByType(taskName);
        if (elementDevisList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(elementDevisList);
    }



    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    @GetMapping("/countByType/{typeLabel}")
    public int countElementDevisByType(@PathVariable String typeLabel) {
        return elementDevisService.nbre_Edevis_par_type(typeLabel);
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    @GetMapping("/detailsByType/{typeLabel}")
    public ResponseEntity<List<ElementDevis>> getDevisDetailsByType(@PathVariable String typeLabel) {
        List<ElementDevis> elementDevisList = elementDevisService.getDevisDetailsByType(typeLabel);
        return ResponseEntity.ok(elementDevisList);
    }
    @GetMapping("/status-distribution")
    public Map<String, Long> getElementDevisStatusDistribution() {
        return elementDevisService.getElementDevisStatusDistribution();
    }
}