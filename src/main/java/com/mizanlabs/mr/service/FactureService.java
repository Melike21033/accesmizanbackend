package com.mizanlabs.mr.service;

import com.mizanlabs.mr.entities.Facture;
import com.mizanlabs.mr.repository.FactureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FactureService {

    private final FactureRepository factureRepository;

    @Autowired
    public FactureService(FactureRepository factureRepository) {
        this.factureRepository = factureRepository;
    }

    public List<Facture> getAllFactures() {
        return factureRepository.findAll();
    }

    public Optional<Facture> getFactureById(Long id) {
        return factureRepository.findById(id);
    }

    public Facture saveFacture(Facture facture) {
        return factureRepository.save(facture);
    }

    public void deleteFacture(Long id) {
        factureRepository.deleteById(id);
    }

    public Facture updateFacture(Long id, Facture factureDetails) {
        return factureRepository.findById(id).map(facture -> {
            facture.setDateCreation(factureDetails.getDateCreation());
            facture.setMontantTotal(factureDetails.getMontantTotal());
            facture.setClient(factureDetails.getClient());
            facture.setElements(factureDetails.getElements());
            return factureRepository.save(facture);
        }).orElseThrow(() -> new ResourceNotFoundException("Facture not found with id " + id));
    }
    
    
}