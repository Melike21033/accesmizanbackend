package com.mizanlabs.mr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mizanlabs.mr.entities.Tache;
import com.mizanlabs.mr.service.TacheService;

import java.util.List;

@RestController
@RequestMapping("/api/taches")
public class TacheController {

    private final TacheService tacheService;

    @Autowired
    public TacheController(TacheService tacheService) {
        this.tacheService = tacheService;
    }

    // Get all taches
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    @GetMapping
    public List<Tache> getAllTaches() {
        return tacheService.findAllTaches();
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    // Get a single tache by ID
    @GetMapping("/{id}")
    public ResponseEntity<Tache> getTacheById(@PathVariable Long id) {
        return tacheService.findTacheById(id)
                .map(tache -> ResponseEntity.ok().body(tache))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    // Create a new tache
    @PostMapping
    public Tache createTache(@RequestBody Tache tache) {
        return tacheService.saveTache(tache);
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    // Update a tache
    @PutMapping("/{id}")
    public ResponseEntity<Tache> updateTache(@PathVariable Long id, @RequestBody Tache tacheDetails) {
        return tacheService.findTacheById(id)
                .map(tache -> {
                    tache.setLabel(tacheDetails.getLabel());
                    // Update any other fields here
                    Tache updatedTache = tacheService.saveTache(tache);
                    return ResponseEntity.ok(updatedTache);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    // Delete a tache
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTache(@PathVariable Long id) {
        return tacheService.findTacheById(id)
                .map(tache -> {
                    tacheService.deleteTache(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Additional controller methods can be added here as needed
}
