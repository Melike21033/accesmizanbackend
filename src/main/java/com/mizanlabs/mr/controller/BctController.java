package com.mizanlabs.mr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mizanlabs.mr.entities.BCT;
import com.mizanlabs.mr.service.BctService;

import java.util.List;

@RestController
@RequestMapping("/api/bcts/admin")
public class BctController {

    private final BctService bctService;

    @Autowired
    public BctController(BctService bctService) {
        this.bctService = bctService;
    }

    // Get all BCTs
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    @GetMapping
    public List<BCT> getAllBCTs() {
        return bctService.findAllBCTs();
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    // Get a single BCT by ID
    @GetMapping("/{id}")
    public ResponseEntity<BCT> getBCTById(@PathVariable Long id) {
        return bctService.findBCTById(id)
                .map(bct -> ResponseEntity.ok().body(bct))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    // Create a new BCT
    @PostMapping
    public BCT createBCT(@RequestBody BCT bct) {
        return bctService.saveBCT(bct);
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    // Update a BCT
    @PutMapping("/{id}")
    public ResponseEntity<BCT> updateBCT(@PathVariable Long id, @RequestBody BCT bctDetails) {
        return bctService.findBCTById(id)
                .map(bct -> {
                    bct.setLabel(bctDetails.getLabel());
                    // Update other fields as necessary
                    BCT updatedBCT = bctService.saveBCT(bct);
                    return ResponseEntity.ok(updatedBCT);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    // Delete a BCT
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBCT(@PathVariable Long id) {
        return bctService.findBCTById(id)
                .map(bct -> {
                    bctService.deleteBCT(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Additional endpoints as needed for your application can be added here
}
