
package com.mizanlabs.mr.controller;

import com.mizanlabs.mr.entities.Priorite;
import com.mizanlabs.mr.service.PrioriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/api/priorites")
public class PrioriteController {
    private final PrioriteService prioriteService;

    @Autowired
    public PrioriteController(PrioriteService prioriteService) {
        this.prioriteService = prioriteService;
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @GetMapping
    public ResponseEntity<List<Priorite>> getAllPriorites() {
        List<Priorite> Priorites = prioriteService.getAllPriorites();
        return new ResponseEntity<>(Priorites, HttpStatus.OK);
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @GetMapping("/{id}")
    public ResponseEntity<Priorite> getPrioriteById(@PathVariable("id") Long id) {
        Optional<Priorite> Priorite = prioriteService.getPrioriteById(id);
        return Priorite.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @PostMapping
    public ResponseEntity<?> createPriorite(@RequestBody Priorite Priorite) {
        // Vérifier si le libellé du Priorite existe déjà
        List<Priorite> existingPriorites = prioriteService.getAllPriorites();
        boolean exists = existingPriorites.stream().anyMatch(t -> t.getLabel().equalsIgnoreCase(Priorite.getLabel()));
        if (exists) {
            return new ResponseEntity<>("Un Priorite avec ce libellé existe déjà", HttpStatus.CONFLICT);
        }

        Priorite createdPriorite = prioriteService.saveOrUpdatePriorite(Priorite);
        return new ResponseEntity<>(createdPriorite, HttpStatus.CREATED);
    }

    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    @PutMapping("/{id}")
    public ResponseEntity<Priorite> updatePriorite(@PathVariable("id") Long id, @RequestBody Priorite Priorite) {
        Optional<Priorite> existingPrioriteOptional = prioriteService.getPrioriteById(id);
        if (existingPrioriteOptional.isPresent()) {
            Priorite existingPriorite = existingPrioriteOptional.get();
            existingPriorite.setLabel(Priorite.getLabel()); // Mise à jour du libellé
            Priorite updatedPriorite = prioriteService.saveOrUpdatePriorite(existingPriorite);
            return new ResponseEntity<>(updatedPriorite, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePriorite(@PathVariable Long id) {
        try {
            prioriteService.deletePriorite(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // Handle specific exceptions and provide more context to the client
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while deleting the priorite");
        }
    }
}
