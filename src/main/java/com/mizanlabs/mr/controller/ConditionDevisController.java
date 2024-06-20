package com.mizanlabs.mr.controller;

import com.mizanlabs.mr.entities.ConditionDevis;
import com.mizanlabs.mr.service.ConditionDevisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/condition-devis")
public class ConditionDevisController {

    @Autowired
    private ConditionDevisService conditionDevisService;
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @GetMapping
    public List<ConditionDevis> getAllConditionDevis() {
        return conditionDevisService.getAllConditionDevis();
    }

    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @GetMapping("/{id}")
    public ResponseEntity<ConditionDevis> getConditionDevisById(@PathVariable Long id) {
        Optional<ConditionDevis> conditionDevis = conditionDevisService.getConditionDevisById(id);
        return conditionDevis.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @GetMapping("/devis/{devisId}")
    public List<ConditionDevis> getConditionsByDevisId(@PathVariable Long devisId) {
        return conditionDevisService.getConditionsByDevisId(devisId);
    }

    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @PostMapping
    public ConditionDevis createConditionDevis(@RequestBody ConditionDevis conditionDevis) {
        return conditionDevisService.saveConditionDevis(conditionDevis);
    }

    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @PutMapping("/{id}")
    public ResponseEntity<ConditionDevis> updateConditionDevis(@PathVariable Long id, @RequestBody ConditionDevis conditionDevis) {
        if (!conditionDevisService.getConditionDevisById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        conditionDevis.setId(id);
        ConditionDevis updatedConditionDevis = conditionDevisService.saveConditionDevis(conditionDevis);
        return ResponseEntity.ok(updatedConditionDevis);
    }

    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConditionDevis(@PathVariable Long id) {
        if (!conditionDevisService.getConditionDevisById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        conditionDevisService.deleteConditionDevis(id);
        return ResponseEntity.noContent().build();
    }
}
