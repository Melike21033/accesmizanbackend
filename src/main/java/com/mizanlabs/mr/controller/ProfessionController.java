
package com.mizanlabs.mr.controller;

import com.mizanlabs.mr.entities.Profession;
import com.mizanlabs.mr.repository.ProfessionRepository;
import com.mizanlabs.mr.service.ProfessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/api/Professions")
public class ProfessionController {
    private final ProfessionService ProfessionService;
private final ProfessionRepository professionRepository;
    @Autowired
    public ProfessionController(ProfessionService ProfessionService, ProfessionRepository professionRepository) {
        this.ProfessionService = ProfessionService;
        this.professionRepository = professionRepository;
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @GetMapping
    public ResponseEntity<List<Profession>> getAllProfessions() {
        List<Profession> Professions = ProfessionService.getAllProfessions();
        return new ResponseEntity<>(Professions, HttpStatus.OK);
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @GetMapping("/{id}")
    public ResponseEntity<Profession> getProfessionById(@PathVariable("id") Long id) {
        Optional<Profession> Profession = ProfessionService.getProfessionById(id);
        return Profession.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @PostMapping
    public ResponseEntity<?> createProfession(@RequestBody Profession Profession) {
        // Vérifier si le libellé du Profession existe déjà
        List<Profession> existingProfessions = ProfessionService.getAllProfessions();
        boolean exists = existingProfessions.stream().anyMatch(t -> t.getLabel().equalsIgnoreCase(Profession.getLabel()));
        if (exists) {
            return new ResponseEntity<>("Un Profession avec ce libellé existe déjà", HttpStatus.CONFLICT);
        }

        Profession createdProfession = ProfessionService.saveOrUpdateProfession(Profession);
        return new ResponseEntity<>(createdProfession, HttpStatus.CREATED);
    }

    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    @PutMapping("/{id}")
    public ResponseEntity<Profession> updateProfession(@PathVariable("id") Long id, @RequestBody Profession Profession) {
        Optional<Profession> existingProfessionOptional = ProfessionService.getProfessionById(id);
        if (existingProfessionOptional.isPresent()) {
            Profession existingProfession = existingProfessionOptional.get();
            existingProfession.setLabel(Profession.getLabel()); // Mise à jour du libellé
            Profession updatedProfession = ProfessionService.saveOrUpdateProfession(existingProfession);
            return new ResponseEntity<>(updatedProfession, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProfession(@PathVariable Long id) {
        try {
            ProfessionService.deleteProfession(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // Handle specific exceptions and provide more context to the client
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while deleting the Profession");
        }
    }
    @GetMapping("/name/{name}")
    public ResponseEntity<Profession> getProfessionByName(@PathVariable String name) {
        List<Profession> professions = professionRepository.findByLabel(name);
        if (professions != null && !professions.isEmpty()) {
            return ResponseEntity.ok(professions.get(0));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
