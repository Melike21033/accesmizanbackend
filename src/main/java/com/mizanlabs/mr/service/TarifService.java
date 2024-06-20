package com.mizanlabs.mr.service;

import com.mizanlabs.mr.entities.Tarif;
import com.mizanlabs.mr.entities.Unite;
import com.mizanlabs.mr.repository.TarifRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TarifService {

    private final TarifRepository tarifRepository;

    @Autowired
    public TarifService(TarifRepository tarifRepository) {
        this.tarifRepository = tarifRepository;
    }

//    public Tarif saveOrUpdateTarif(Tarif tarif) {
//        // If the tarif is marked as principal, ensure it's the only principal tarif for that element
//        if (Boolean.TRUE.equals(tarif.getPrincipal())) {
//            // Fetch all principal tarifs for the same element as the current tarif
//            List<Tarif> principalTarifs = tarifRepository.findByIsPrincipalAndElementId(true, tarif.getElement().getId());
//
//            // Reset the principal flag for other tarifs
//            for (Tarif existingTarif : principalTarifs) {
//                // Skip the current tarif being saved or updated
//                if (!existingTarif.getId().equals(tarif.getId())) {
//                    existingTarif.setPrincipal(false);
//                    tarifRepository.save(existingTarif);
//                }
//            }
//        } else {
//            // If the current tarif is not principal, there's no need to modify other tarifs
//            // However, ensure there's at least one principal tarif if required by business logic
//        }
//
//        return tarifRepository.save(tarif);
//    }
    public Tarif saveOrUpdateTarif(Tarif tarif) {
        // Vérifier si le tarif actuel est marqué comme ayant l'unité principale
        if (Boolean.TRUE.equals(tarif.getPrincipal())) {
            // Trouver tous les tarifs pour le même élément que le tarif actuel
            List<Tarif> tarifsForElement = tarifRepository.findByElementId(tarif.getElement().getId());

            // Parcourir tous les tarifs et réinitialiser le flag principal pour les autres unités
            for (Tarif existingTarif : tarifsForElement) {
                if (!existingTarif.getId().equals(tarif.getId()) && existingTarif.getPrincipal()) {
                    existingTarif.setPrincipal(false);
                    tarifRepository.save(existingTarif);
                }
            }
        } else if (!tarifRepository.existsByElementIdAndIsPrincipal(tarif.getElement().getId(), true)) {
            // S'il n'existe aucun tarif principal pour cet élément, marquer le tarif actuel comme principal
            tarif.setPrincipal(true);
        }

        // Sauvegarder ou mettre à jour le tarif
        return tarifRepository.save(tarif);
    }

    public List<Unite> getUnitesNotLinkedToElement(Long elementId) {
        return tarifRepository.findUnitesNotLinkedToElement(elementId);
    }

    public List<Tarif> getAllTarifs() {
        return tarifRepository.findAll();
    }

    public Optional<Tarif> getTarifById(Long id) {
        return tarifRepository.findById(id);
    }

    public void deleteTarifById(Long id) {
        tarifRepository.deleteById(id);
    }
    // Dans TarifService.java
    public boolean existsAnyPrincipalTarif() {
        return tarifRepository.existsByIsPrincipal(true);
    }
    public boolean existsPrincipalTarifForElement(Long elementId) {
        return tarifRepository.existsByElementIdAndIsPrincipal(elementId, true);
    }
    public List<Tarif> getTarifsByElementId(Long elementId) {
        // Implémentez la logique pour récupérer les tarifs par ID d'élément
        // Ceci est un exemple, adaptez-le à votre implémentation de TarifRepository
        return tarifRepository.findByElementId(elementId);
    }
//    public Integer getPrixUnitaireByElementIdAndUniteId(Long elementId, Long uniteId) {
//        return tarifRepository.findTarifByElementIdAndUniteId(elementId, uniteId)
//                .map(Tarif::getPritunit)
//                .orElseThrow(() -> new RuntimeException("Tarif not found"));
//    }
    
    public Integer getPrixUnitaireByElementIdAndUniteNom(Long elementId, String uniteNom) {
        return tarifRepository.findTarifByElementIdAndUniteNom(elementId, uniteNom)
                .map(Tarif::getPritunit)
                .orElseThrow(() -> new RuntimeException("Tarif not found"));
    }

}
