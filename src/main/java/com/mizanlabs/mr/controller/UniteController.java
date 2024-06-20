//UniteController
package com.mizanlabs.mr.controller;

import com.mizanlabs.mr.entities.Unite;
import org.springframework.dao.DataIntegrityViolationException;

import com.mizanlabs.mr.service.UniteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/unites")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class UniteController {

    private final UniteService uniteService;

    @Autowired
    public UniteController(UniteService uniteService) {
        this.uniteService = uniteService;
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @GetMapping
    public ResponseEntity<List<Unite>> getAllUnites() {
        List<Unite> unites = uniteService.getAllUnites();
        return new ResponseEntity<>(unites, HttpStatus.OK);
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @GetMapping("/{id}")
    public ResponseEntity<Unite> getUniteById(@PathVariable("id") Long id) {
        Optional<Unite> unite = uniteService.getUniteById(id);
        return unite.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @PostMapping
    public ResponseEntity<?> createUnite(@RequestBody Unite unite) {
        try {
            Unite createdUnite = uniteService.saveOrUpdateUnite(unite);
            return new ResponseEntity<>(createdUnite, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException ex) {
            // Ici, vous pouvez personnaliser le message d'erreur basé sur l'exception
            String errorMessage = "Une entité avec cette valeur existe déjà.";
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            // Gérer d'autres exceptions inattendues
            String errorMessage = "Erreur lors de la création de l'unité: " + ex.getMessage();
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @PutMapping("/{id}")
    public ResponseEntity<Unite> updateUnite(@PathVariable("id") Long id, @RequestBody Unite unite) {
        Optional<Unite> existingUniteOptional = uniteService.getUniteById(id);
        if (existingUniteOptional.isPresent()) {
            Unite existingUnite = existingUniteOptional.get();
            existingUnite.setUnite(unite.getUnite()); // Update the unit
            Unite updatedUnite = uniteService.saveOrUpdateUnite(existingUnite);
            return new ResponseEntity<>(updatedUnite, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUnite(@PathVariable("id") Long id) {
        Optional<Unite> uniteOptional = uniteService.getUniteById(id);
        if (uniteOptional.isPresent()) {
            uniteService.deleteUniteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
