package com.mizanlabs.mr.service;

import com.mizanlabs.mr.entities.Unite;
import com.mizanlabs.mr.repository.TarifRepository;
import com.mizanlabs.mr.repository.UniteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UniteService {

    private final UniteRepository uniteRepository;
    private final TarifRepository tarifRepository;
    @Autowired
    public UniteService(UniteRepository uniteRepository, TarifRepository tarifRepository) {
        this.uniteRepository = uniteRepository;
        this.tarifRepository = tarifRepository;
    }

    public Unite saveOrUpdateUnite(Unite unite) {
        return uniteRepository.save(unite);
    }

    public List<Unite> getAllUnites() {
        return uniteRepository.findAll();
    }

    public Optional<Unite> getUniteById(Long id) {
        return uniteRepository.findById(id);
    }

    public void deleteUniteById(Long id) {
        Optional<Unite> uniteOptional = uniteRepository.findById(id);
        if (uniteOptional.isPresent()) {
            Unite unite = uniteOptional.get();
            // Supprimer les tarifs associés à cette unité
//            tarifRepository.deleteByUnite(unite);
            // Ensuite, supprimer l'unité elle-même
            uniteRepository.delete(unite);
        }
    }
}
