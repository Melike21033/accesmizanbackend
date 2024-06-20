package com.mizanlabs.mr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mizanlabs.mr.entities.BCT;
import com.mizanlabs.mr.repository.BctRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BctService {

    private final BctRepository bctRepository;

    @Autowired
    public BctService(BctRepository bctRepository) {
        this.bctRepository = bctRepository;
    }

    public List<BCT> findAllBCTs() {
        return bctRepository.findAll();
    }

    public Optional<BCT> findBCTById(Long id) {
        return bctRepository.findById(id);
    }

    public BCT saveBCT(BCT bct) {
        return bctRepository.save(bct);
    }

    public void deleteBCT(Long id) {
        bctRepository.deleteById(id);
    }

    // Additional methods for your business logic could be added here as needed.
}
