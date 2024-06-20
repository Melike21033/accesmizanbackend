package com.mizanlabs.mr.service;

import com.mizanlabs.mr.entities.LigneDevis;
import com.mizanlabs.mr.repository.LignesDevisRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LigneDevisService {

    private final LignesDevisRepository ligneDevisRepository;

    @Autowired
    public LigneDevisService(LignesDevisRepository ligneDevisRepository) {
        this.ligneDevisRepository = ligneDevisRepository;
    }

    public LigneDevis createLigneDevis(LigneDevis ligneDevis) {
        return ligneDevisRepository.save(ligneDevis);
    }
    
    public List<LigneDevis> getAllLignesDevis() {
        return ligneDevisRepository.findAll();
    }

    public Optional<LigneDevis> getLigneDevisById(Long id) {
        return ligneDevisRepository.findById(id);
    }

    public Optional<LigneDevis> updateLigneDevis(Long id, LigneDevis ligneDevis) {
        if (ligneDevisRepository.existsById(id)) {
            ligneDevis.setId(id);
            return Optional.of(ligneDevisRepository.save(ligneDevis));
        } else {
            return Optional.empty();
        }
    }

    public boolean deleteLigneDevis(Long id) {
        if (ligneDevisRepository.existsById(id)) {
            ligneDevisRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
    
    public List<String> getTaskNamesByProjectName(String projectName) {
        return ligneDevisRepository.findTaskNamesByProjectName(projectName);
    }  

    public List<LigneDevis> getDistinctItemsByTaskId(Long taskId) {
        return ligneDevisRepository.findDistinctByTaskId(taskId);
    }
}
