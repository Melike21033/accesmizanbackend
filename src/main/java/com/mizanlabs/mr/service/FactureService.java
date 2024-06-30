package com.mizanlabs.mr.service;

import com.mizanlabs.mr.entities.Devis;
import com.mizanlabs.mr.entities.ElementDevis;
import com.mizanlabs.mr.entities.Facture;
import com.mizanlabs.mr.entities.Status;
import com.mizanlabs.mr.entities.Task;
import com.mizanlabs.mr.repository.DevisRepository;
import com.mizanlabs.mr.repository.ElementDevisRepository;
import com.mizanlabs.mr.repository.FactureRepository;
import com.mizanlabs.mr.repository.StatusRepository;
import com.mizanlabs.mr.repository.TaskRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class FactureService {

    private final FactureRepository factureRepository;
    private final DevisRepository devisRepository;
    private final ElementDevisRepository elementDevisRepository;
    private final TaskRepository taskRepository;
    private final StatusRepository statusRepository;

    @Autowired
    public FactureService(StatusRepository statusRepository,TaskRepository taskRepository,FactureRepository factureRepository,DevisRepository devisRepository,ElementDevisRepository elementDevisRepository) {
        this.factureRepository = factureRepository;
        this.devisRepository = devisRepository;
        this.elementDevisRepository = elementDevisRepository;
        this.taskRepository = taskRepository;
        this.statusRepository = statusRepository;


    }
    
    public List<Facture> genererFacturesPourDevis(Devis devis) {
        List<Facture> factures = new ArrayList<>();

        // Récupération des modalités de paiement et de leurs pourcentages
        String[] modalitesPaiement = {devis.getPMP1(), devis.getPMP2(), devis.getPMP3(), devis.getPMP4(), devis.getPMP5()};
        String[] pourcentagesStr = {devis.getMP1(), devis.getMP2(), devis.getMP3(), devis.getMP4(), devis.getMP5()};

        // Génération des factures basées sur les modalités de paiement
        for (int i = 0; i < modalitesPaiement.length; i++) {
            if (modalitesPaiement[i] != null && pourcentagesStr[i] != null) {
                try {
                    if (!modalitesPaiement[i].equalsIgnoreCase("Proportionnellement aux travaux réalisés")) {
                        int pourcentage = Integer.parseInt(pourcentagesStr[i]);
                        Double montantTotal = devis.getMontant() * (pourcentage / 100.0);

                        Facture facture = new Facture();
                        facture.setDateCreation(new Date());
                        facture.setMontantTotal(montantTotal);
                        facture.setMontantResiduel(montantTotal);
                        facture.setClient(devis.getProject().getClient());
                        facture.setDevis(devis);
                        facture.setModalite(modalitesPaiement[i]);

                        factures.add(facture);
                    }
                } catch (NumberFormatException e) {
                    // Gérer l'exception si le pourcentage ne peut pas être converti en entier
                    e.printStackTrace();
                }
            }
        }

        // Sauvegarder les factures générées
        return factureRepository.saveAll(factures);
    }
    
    public ElementDevis affecterElementDevisAFacture(Long elementDevisId, Long factureId) {
        Optional<Facture> optionalFacture = factureRepository.findById(factureId);
        Optional<ElementDevis> optionalElementDevis = elementDevisRepository.findById(elementDevisId);

        if (optionalFacture.isPresent() && optionalElementDevis.isPresent()) {
            Facture facture = optionalFacture.get();
            ElementDevis elementDevis = optionalElementDevis.get();

            elementDevis.setFacture(facture);

            return elementDevisRepository.save(elementDevis);
        } else {
            throw new RuntimeException("Facture or ElementDevis not found");
        }
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

    public void affecterTacheAFacture(Long taskId, Long factureId) {
        Optional<Facture> optionalFacture = factureRepository.findById(factureId);
        Optional<Task> optionalTask = taskRepository.findById(taskId);

        if (optionalFacture.isPresent() && optionalTask.isPresent()) {
            Facture facture = optionalFacture.get();
            Task task = optionalTask.get();

            Set<ElementDevis> elementsDevis = task.getElementDevis();

            for (ElementDevis elementDevis : elementsDevis) {
                elementDevis.setFacture(facture);
                elementDevisRepository.save(elementDevis);
            }
        } else {
            if (!optionalFacture.isPresent()) {
                throw new RuntimeException("Facture not found with id: " + factureId);
            }
            if (!optionalTask.isPresent()) {
                throw new RuntimeException("Task not found with id: " + taskId);
            }
            throw new RuntimeException("Facture or Task not found");
        }
    }
    

    public List<Facture> validerDevisEtGenererFactures(Long devisId) {
        Optional<Devis> optionalDevis = devisRepository.findById(devisId);

        if (optionalDevis.isPresent()) {
            Devis devis = optionalDevis.get();
            Status statusValide = statusRepository.findByLabelAndTableref("Validé", "devis");

            if (statusValide != null) {
                devis.setStatus(statusValide);
                devisRepository.save(devis);

                return genererFacturesPourDevis(devis);
            } else {
                throw new RuntimeException("Status 'Validé' not found for 'devis'");
            }
        } else {
            throw new RuntimeException("Devis not found with id: " + devisId);
        }
    }
    
    public List<Facture> getFacturesByClientId(Long clientId) {
        return factureRepository.findByClientId(clientId);
    }
    
}