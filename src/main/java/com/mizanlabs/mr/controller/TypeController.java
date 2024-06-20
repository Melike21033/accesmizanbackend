package com.mizanlabs.mr.controller;

import com.mizanlabs.mr.entities.Type;
import com.mizanlabs.mr.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/types")
public class TypeController {

    private final TypeService typeService;

    @Autowired
    public TypeController(TypeService typeService) {
        this.typeService = typeService;
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @GetMapping
    public ResponseEntity<List<Type>> getAllTypes() {
        List<Type> types = typeService.getAllTypes();
        return new ResponseEntity<>(types, HttpStatus.OK);
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @GetMapping("/{id}")
    public ResponseEntity<Type> getTypeById(@PathVariable("id") Long id) {
        Optional<Type> type = typeService.getTypeById(id);
        return type.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    @PostMapping
    public ResponseEntity<?> createType(@RequestBody Type type) {
        // Vérifier si le libellé du type existe déjà
        List<Type> existingTypes = typeService.getAllTypes();
        boolean exists = existingTypes.stream().anyMatch(t -> t.getLabel().equalsIgnoreCase(type.getLabel()));
        if (exists) {
            return new ResponseEntity<>("Un type avec ce libellé existe déjà", HttpStatus.CONFLICT);
        }

        Type createdType = typeService.saveOrUpdateType(type);
        return new ResponseEntity<>(createdType, HttpStatus.CREATED);
    }

    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    @PutMapping("/{id}")
    public ResponseEntity<Type> updateType(@PathVariable("id") Long id, @RequestBody Type type) {
        Optional<Type> existingTypeOptional = typeService.getTypeById(id);
        if (existingTypeOptional.isPresent()) {
            Type existingType = existingTypeOptional.get();
            existingType.setLabel(type.getLabel()); // Mise à jour du libellé
            Type updatedType = typeService.saveOrUpdateType(existingType);
            return new ResponseEntity<>(updatedType, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteType(@PathVariable("id") Long id) {
        Optional<Type> typeOptional = typeService.getTypeById(id);
        if (typeOptional.isPresent()) {
            typeService.deleteTypeById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
