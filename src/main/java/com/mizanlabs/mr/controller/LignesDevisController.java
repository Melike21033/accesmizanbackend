package com.mizanlabs.mr.controller;

import com.mizanlabs.mr.entities.LigneDevis;
import com.mizanlabs.mr.service.LigneDevisService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lignes-devis")
public class LignesDevisController {

    private final LigneDevisService ligneDevisService;

    @Autowired
    public LignesDevisController(LigneDevisService ligneDevisService) {
        this.ligneDevisService = ligneDevisService;
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    @PostMapping
    public ResponseEntity<LigneDevis> createLigneDevis(@RequestBody LigneDevis ligneDevis) {
        LigneDevis savedLigneDevis = ligneDevisService.createLigneDevis(ligneDevis);
        return ResponseEntity.ok(savedLigneDevis);
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    @GetMapping
    public List<LigneDevis> getAllLignesDevis() {
        return ligneDevisService.getAllLignesDevis();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LigneDevis> getLigneDevisById(@PathVariable Long id) {
        return ligneDevisService.getLigneDevisById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    @PutMapping("/{id}")
    public ResponseEntity<LigneDevis> updateLigneDevis(@PathVariable Long id, @RequestBody LigneDevis ligneDevis) {
        return ligneDevisService.updateLigneDevis(id, ligneDevis)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLigneDevis(@PathVariable Long id) {
        if (ligneDevisService.deleteLigneDevis(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    @GetMapping("/taskNames/{projectName}")
    public List<String> getTaskNamesByProjectName(@PathVariable String projectName) {
        return ligneDevisService.getTaskNamesByProjectName(projectName);
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    @GetMapping("/items/{taskId}")
    public ResponseEntity<List<LigneDevis>> getDistinctItemsByTaskId(@PathVariable Long taskId) {
        List<LigneDevis> distinctItems = ligneDevisService.getDistinctItemsByTaskId(taskId);
        if (!distinctItems.isEmpty()) {
            return ResponseEntity.ok(distinctItems);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
