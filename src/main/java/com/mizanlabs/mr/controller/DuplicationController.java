package com.mizanlabs.mr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.mizanlabs.mr.entities.Duplication;
import com.mizanlabs.mr.service.DuplicationService;

@RestController

public class DuplicationController {

    private final DuplicationService duplicationService;

    @Autowired
    public DuplicationController(DuplicationService duplicationService) {
        this.duplicationService = duplicationService;
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    @GetMapping("/check-duplication")
    public boolean checkDuplication(@RequestParam Long d, @RequestParam Long p) {
        return duplicationService.existsByDAndP(d, p);
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    @PostMapping("/api/duplications")
    public Duplication addDuplication(@RequestParam Long devisId, @RequestParam Long projectId) {
        return duplicationService.addDuplication(devisId, projectId);
    }
}