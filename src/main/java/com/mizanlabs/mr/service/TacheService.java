package com.mizanlabs.mr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mizanlabs.mr.entities.Tache;
import com.mizanlabs.mr.repository.TacheRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TacheService {

    private final TacheRepository tacheRepository;

    @Autowired
    public TacheService(TacheRepository tacheRepository) {
        this.tacheRepository = tacheRepository;
    }

    public List<Tache> findAllTaches() {
        return tacheRepository.findAll();
    }

    public Optional<Tache> findTacheById(Long id) {
        return tacheRepository.findById(id);
    }

    public Tache saveTache(Tache tache) {
        return tacheRepository.save(tache);
    }

    public void deleteTache(Long id) {
        tacheRepository.deleteById(id);
    }

    // Add any other necessary service methods here.
}
