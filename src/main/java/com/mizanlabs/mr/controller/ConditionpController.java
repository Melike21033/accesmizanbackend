package com.mizanlabs.mr.controller;

import com.mizanlabs.mr.entities.Conditionp;
import com.mizanlabs.mr.service.ConditionpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/conditions")
public class ConditionpController {

    private final ConditionpService conditionpService;

    @Autowired
    public ConditionpController(ConditionpService conditionpService) {
        this.conditionpService = conditionpService;
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @GetMapping
    public ResponseEntity<List<Conditionp>> getAllConditions() {
        List<Conditionp> conditions = conditionpService.getAllConditionps();
        return new ResponseEntity<>(conditions, HttpStatus.OK);
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @GetMapping("/{id}")
    public ResponseEntity<Conditionp> getConditionById(@PathVariable Long id) {
        Optional<Conditionp> condition = conditionpService.getConditionpById(id);
        return condition.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @PostMapping
    public ResponseEntity<Conditionp> createCondition(@RequestBody Conditionp conditionp) {
        Conditionp createdCondition = conditionpService.saveOrUpdateConditionp(conditionp);
        return new ResponseEntity<>(createdCondition, HttpStatus.CREATED);
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @PutMapping("/{id}")
    public ResponseEntity<Conditionp> updateCondition(@PathVariable Long id, @RequestBody Conditionp conditionpDetails) {
        Optional<Conditionp> conditionOptional = conditionpService.getConditionpById(id);
        if (!conditionOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Conditionp conditionToUpdate = conditionOptional.get();
        conditionToUpdate.setLabel(conditionpDetails.getLabel());
        Conditionp updatedCondition = conditionpService.saveOrUpdateConditionp(conditionToUpdate);
        return new ResponseEntity<>(updatedCondition, HttpStatus.OK);
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCondition(@PathVariable Long id) {
        conditionpService.deleteConditionp(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
