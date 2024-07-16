package com.mizanlabs.mr.controller;

import com.mizanlabs.mr.entities.Status;
import com.mizanlabs.mr.repository.StatusRepository;
import com.mizanlabs.mr.service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/Status")
public class StatusController {
    private final StatusService StatusService;
    private final StatusRepository statusRepository;
    @Autowired
    public StatusController(StatusService StatusService, StatusRepository statusRepository) {
        this.StatusService = StatusService;
        this.statusRepository = statusRepository;
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @GetMapping
    public ResponseEntity<List<Status>> getAllStatuss() {
        List<Status> Statuss = StatusService.getAllStatuss();
        return new ResponseEntity<>(Statuss, HttpStatus.OK);
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @GetMapping("/{id}")
    public ResponseEntity<Status> getStatusById(@PathVariable("id") Long id) {
        Optional<Status> Status = StatusService.getStatusById(id);
        return Status.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    @PostMapping
    public ResponseEntity<?> createStatus(@RequestBody Status Status) {
        // Vérifier si le libellé du Status existe déjà
        List<Status> existingStatuss = StatusService.getAllStatuss();
        boolean exists = existingStatuss.stream().anyMatch(t -> t.getLabel().equalsIgnoreCase(Status.getLabel()));
        if (exists) {
            return new ResponseEntity<>("Un Status avec ce libellé existe déjà", HttpStatus.CONFLICT);
        }

        Status createdStatus = StatusService.saveOrUpdateStatus(Status);
        return new ResponseEntity<>(createdStatus, HttpStatus.CREATED);
    }

    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    @PutMapping("/{id}")
    public ResponseEntity<Status> updateStatus(@PathVariable("id") Long id, @RequestBody Status Status) {
        Optional<Status> existingStatusOptional = StatusService.getStatusById(id);
        if (existingStatusOptional.isPresent()) {
            Status existingStatus = existingStatusOptional.get();
            existingStatus.setLabel(Status.getLabel()); // Mise à jour du libellé
            existingStatus.setTableref(Status.getTableref()); // Mise à jour de la table
            Status updatedStatus = StatusService.saveOrUpdateStatus(existingStatus);
            return new ResponseEntity<>(updatedStatus, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStatus(@PathVariable Long id) {
        try {
            StatusService.deleteStatus(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // Handle specific exceptions and provide more context to the client
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while deleting the Status");
        }
    }
    @GetMapping("/label/{label}/tableref/{tableref}")
    public ResponseEntity<Status> getStatusByLabelAndTableref(@PathVariable String label, @PathVariable String tableref) {
        List<Status> statuses = statusRepository.findByTablerefAndLabel(tableref,label);
        if (statuses != null && !statuses.isEmpty()) {
            return ResponseEntity.ok(statuses.get(0));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
