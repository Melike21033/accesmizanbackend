package com.mizanlabs.mr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mizanlabs.mr.entities.Duplication;
import com.mizanlabs.mr.repository.DuplicationRepository;

@Service
public class DuplicationService {

    private final DuplicationRepository duplicationRepository;

    @Autowired
    public DuplicationService(DuplicationRepository duplicationRepository) {
        this.duplicationRepository = duplicationRepository;
    }

    public boolean existsByDAndP(Long d, Long p) {
        return duplicationRepository.existsByDAndP(d, p);
    }

    public Duplication addDuplication(Long devisId, Long projectId) {
        Duplication duplication = new Duplication();
        duplication.setD(devisId);
        duplication.setP(projectId);
        return duplicationRepository.save(duplication);
    }
}