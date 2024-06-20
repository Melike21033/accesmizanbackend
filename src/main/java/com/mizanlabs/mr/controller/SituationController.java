
package com.mizanlabs.mr.controller;

import com.mizanlabs.mr.entities.Situation;
import com.mizanlabs.mr.service.SituationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/Situations")
public class SituationController {
    private final SituationService situationService;

    @Autowired
    public SituationController(SituationService situationService) {
        this.situationService = situationService;
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @GetMapping
    public ResponseEntity<List<Situation>> getAllSituations() {
        List<Situation> Situations = situationService.getAllSituations();
        return new ResponseEntity<>(Situations, HttpStatus.OK);
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @GetMapping("/{id}")
    public ResponseEntity<Situation> getSituationById(@PathVariable("id") Long id) {
        Optional<Situation> Situation = situationService.getSituationById(id);
        return Situation.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    @PostMapping
    public ResponseEntity<?> createSituation(@RequestBody Situation Situation) {
        // Vérifier si le libellé du Situation existe déjà
        List<Situation> existingSituations = situationService.getAllSituations();
        boolean exists = existingSituations.stream().anyMatch(t -> t.getLabel().equalsIgnoreCase(Situation.getLabel()));
        if (exists) {
            return new ResponseEntity<>("Un Situation avec ce libellé existe déjà", HttpStatus.CONFLICT);
        }

        Situation createdSituation = situationService.saveOrUpdateSituation(Situation);
        return new ResponseEntity<>(createdSituation, HttpStatus.CREATED);
    }

    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    @PutMapping("/{id}")
    public ResponseEntity<Situation> updateSituation(@PathVariable("id") Long id, @RequestBody Situation Situation) {
        Optional<Situation> existingSituationOptional = situationService.getSituationById(id);
        if (existingSituationOptional.isPresent()) {
            Situation existingSituation = existingSituationOptional.get();
            existingSituation.setLabel(Situation.getLabel()); // Mise à jour du libellé
            Situation updatedSituation = situationService.saveOrUpdateSituation(existingSituation);
            return new ResponseEntity<>(updatedSituation, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSituation(@PathVariable Long id) {
        try {
            situationService.deleteSituation(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // Handle specific exceptions and provide more context to the client
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while deleting the Situation");
        }
    }
}
