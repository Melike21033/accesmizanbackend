//ElementController
package com.mizanlabs.mr.controller;

import com.mizanlabs.mr.entities.Element;
import com.mizanlabs.mr.entities.Type;
import com.mizanlabs.mr.entities.Unite;
import com.mizanlabs.mr.entities.UnitesResponse;
import com.mizanlabs.mr.service.ElementService;
import com.mizanlabs.mr.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;

@RestController
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RequestMapping("/api/elements") // Endpoint for the elements API
public class ElementController {

    private final ElementService elementService;
    private final TypeService typeService;

    @Autowired
    public ElementController(ElementService elementService,TypeService typeService) {
        this.elementService = elementService;
        this.typeService=typeService;
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @GetMapping
    public ResponseEntity<List<Element>> getAllElements() {
        List<Element> elements = elementService.getAllElements();
        return new ResponseEntity<>(elements, HttpStatus.OK);
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @GetMapping("/types")
    public ResponseEntity<List<Type>> getAllTypes() {
        List<Type> types = typeService.getAllTypes();
        return new ResponseEntity<>(types, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @GetMapping("/{id}")
    public ResponseEntity<Element> getElementById(@PathVariable("id") Long id) {
        Optional<Element> element = elementService.getElementById(id);
        return element.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @PostMapping
    public ResponseEntity<?> createElement(@RequestBody Element element) {
        System.out.println("Received Element: " + element);
        // Vérifiez si l'ID du type est présent dans l'élément
        if (element.getType() != null && element.getType().getId_type() != null) { // Assurez-vous que c'est getId() et non getId_type()
            Optional<Type> typeOptional = typeService.getTypeById(element.getType().getId_type());
            if (!typeOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Type not found.");
            }
            element.setType(typeOptional.get());
        } else {
            return ResponseEntity.badRequest().body("Type information is missing or incorrect.");
        }

        try {
            Element createdElement = elementService.saveOrUpdateElement(element);
            return new ResponseEntity<>(createdElement, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            // Gestion d'une violation d'intégrité des données, par exemple, un nom d'élément dupliqué
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            // Gestion des autres exceptions inattendues
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @PutMapping("/{id}")
    public ResponseEntity<Element> updateElement(@PathVariable("id") Long id, @RequestBody Element element) {
        Optional<Element> existingElementOptional = elementService.getElementById(id);
        if (existingElementOptional.isPresent()) {
            Element existingElement = existingElementOptional.get();
            existingElement.setName(element.getName());
            existingElement.setNote(element.getNote());
            existingElement.setType(element.getType());
            Element updatedElement = elementService.saveOrUpdateElement(existingElement);
            return new ResponseEntity<>(updatedElement, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteElement(@PathVariable("id") Long id) {
        Optional<Element> elementOptional = elementService.getElementById(id);
        if (elementOptional.isPresent()) {
            elementService.deleteElementById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    @GetMapping("/{elementId}/type")
    public ResponseEntity<Type> getElementTypeById(@PathVariable Long elementId) {
        return elementService.findElementTypeById(elementId)
                .map(type -> ResponseEntity.ok().body(type))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    @GetMapping("/{elementId}/unites")
    public ResponseEntity<UnitesResponse> getUnitesByElementId(@PathVariable Long elementId) {
        UnitesResponse response = elementService.getUnitesByElementId(elementId);
        return ResponseEntity.ok(response);
    }
}
