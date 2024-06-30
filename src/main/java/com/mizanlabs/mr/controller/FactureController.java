package com.mizanlabs.mr.controller;

import com.mizanlabs.mr.entities.Devis;
import com.mizanlabs.mr.entities.ElementDevis;
import com.mizanlabs.mr.entities.Facture;
import com.mizanlabs.mr.service.DevisService;
import com.mizanlabs.mr.service.FactureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/factures")
public class FactureController {

    private final FactureService factureService;
    private final DevisService devisService;

    @Autowired
    public FactureController(FactureService factureService,DevisService devisService) {
        this.factureService = factureService;
        this.devisService = devisService;

    }

    @GetMapping
    public List<Facture> getAllFactures() {
        return factureService.getAllFactures();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Facture> getFactureById(@PathVariable Long id) {
        return factureService.getFactureById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Facture createFacture(@RequestBody Facture facture) {
        return factureService.saveFacture(facture);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Facture> updateFacture(@PathVariable Long id, @RequestBody Facture factureDetails) {
        return ResponseEntity.ok(factureService.updateFacture(id, factureDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFacture(@PathVariable Long id) {
        factureService.deleteFacture(id);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/generer/{devisId}")
    public List<Facture> genererFactures(@PathVariable Long devisId) {
        Optional<Devis> optionalDevis = devisService.getDevisById(devisId);
        if (optionalDevis.isPresent()) {
            Devis devis = optionalDevis.get();
            return factureService.genererFacturesPourDevis(devis);
        } else {
            // Gérer le cas où le devis n'existe pas
            throw new RuntimeException("Devis not found with id: " + devisId);
        }
    }
    
    @PutMapping("/affecter/{elementDevisId}/{factureId}")
    public ResponseEntity<ElementDevis> affecterElementDevisAFacture(@PathVariable Long elementDevisId, @PathVariable Long factureId) {
        ElementDevis updatedElementDevis = factureService.affecterElementDevisAFacture(elementDevisId, factureId);
        return ResponseEntity.ok(updatedElementDevis);
    }
    

    @PutMapping("/affecter-tache/{taskId}/{factureId}")
    public ResponseEntity<?> affecterTacheAFacture(@PathVariable Long taskId, @PathVariable Long factureId) {
        try {
            factureService.affecterTacheAFacture(taskId, factureId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<Facture>> getFacturesByClientId(@PathVariable Long clientId) {
        List<Facture> factures = factureService.getFacturesByClientId(clientId);
        if (factures != null && !factures.isEmpty()) {
            return ResponseEntity.ok(factures);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
    
}