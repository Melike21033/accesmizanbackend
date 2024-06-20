//TarifController
package com.mizanlabs.mr.controller;

import com.mizanlabs.mr.entities.Tarif;
import com.mizanlabs.mr.entities.Unite;
import com.mizanlabs.mr.service.TarifService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tarifs") // Endpoint for the tarifs API
public class TarifController {

    private final TarifService tarifService;

    @Autowired
    public TarifController(TarifService tarifService) {
        this.tarifService = tarifService;
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @GetMapping
    public ResponseEntity<List<Tarif>> getAllTarifs() {
        List<Tarif> tarifs = tarifService.getAllTarifs();
        return new ResponseEntity<>(tarifs, HttpStatus.OK);
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @GetMapping("/unites-not-linked/{elementId}")
    public ResponseEntity<List<Unite>> getUnitesNotLinkedToElement(@PathVariable Long elementId) {
        List<Unite> unites = tarifService.getUnitesNotLinkedToElement(elementId);
        return new ResponseEntity<>(unites, HttpStatus.OK);
    }
//    // Dans TarifController.java
//    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
//    @GetMapping("/principal/exists/{elementId}")
//    public ResponseEntity<Boolean> checkIfAnyPrincipalTarifExists() {
//        boolean exists = tarifService.existsAnyPrincipalTarif();
//        return new ResponseEntity<>(exists, HttpStatus.OK);
//    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @GetMapping("/principal/exists/{elementId}")
    public ResponseEntity<Boolean> checkIfPrincipalTarifExistsForElement(@PathVariable Long elementId) {
        boolean exists = tarifService.existsPrincipalTarifForElement(elementId);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @GetMapping("/byElement/{elementId}")
    public ResponseEntity<List<Tarif>> getTarifsByElementId(@PathVariable("elementId") Long elementId) {
        List<Tarif> tarifs = tarifService.getTarifsByElementId(elementId);
        if (tarifs.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(tarifs, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @GetMapping("/{id}")
    public ResponseEntity<Tarif> getTarifById(@PathVariable("id") Long id) {
        Optional<Tarif> tarif = tarifService.getTarifById(id);
        return tarif.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @PostMapping
    public ResponseEntity<Tarif> createTarif(@RequestBody Tarif tarif) {
        Tarif createdTarif = tarifService.saveOrUpdateTarif(tarif);
        return new ResponseEntity<>(createdTarif, HttpStatus.CREATED);
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @PutMapping("/{id}")
    public ResponseEntity<Tarif> updateTarif(@PathVariable("id") Long id, @RequestBody Tarif tarif) {
        Optional<Tarif> existingTarifOptional = tarifService.getTarifById(id);
        if (existingTarifOptional.isPresent()) {
            Tarif existingTarif = existingTarifOptional.get();

            // Update existingTarif with the new information from tarif
            existingTarif.setUnite(tarif.getUnite());
            existingTarif.setPritunit(tarif.getPritunit());
            existingTarif.setPrincipal(tarif.getPrincipal());  // Update isPrincipal field

            // Save the updated tarif
            Tarif updatedTarif = tarifService.saveOrUpdateTarif(existingTarif);
            return new ResponseEntity<>(updatedTarif, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTarif(@PathVariable("id") Long id) {
        Optional<Tarif> tarifOptional = tarifService.getTarifById(id);
        if (tarifOptional.isPresent()) {
            tarifService.deleteTarifById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

//    @GetMapping("/{elementId}/{uniteId}")
//    public ResponseEntity<Integer> getPrixUnitaireByElementIdAndUniteId(@PathVariable Long elementId, @PathVariable Long uniteId) {
//        Integer prixUnitaire = tarifService.getPrixUnitaireByElementIdAndUniteId(elementId, uniteId);
//        return ResponseEntity.ok(prixUnitaire);
//    }
    @GetMapping("/{elementId}/{uniteNom}")
    public ResponseEntity<Integer> getPrixUnitaireByElementIdAndUniteNom(@PathVariable Long elementId, @PathVariable String uniteNom) {
        Integer prixUnitaire = tarifService.getPrixUnitaireByElementIdAndUniteNom(elementId, uniteNom);
        return ResponseEntity.ok(prixUnitaire);
    }

}
