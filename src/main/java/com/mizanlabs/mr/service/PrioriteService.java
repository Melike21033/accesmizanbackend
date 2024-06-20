
package com.mizanlabs.mr.service;

import com.mizanlabs.mr.entities.Priorite;
import com.mizanlabs.mr.repository.ElementRepository;
import com.mizanlabs.mr.repository.PrioriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Service
public class PrioriteService {
    private final PrioriteRepository PrioriteRepository;
    private final ElementRepository elementRepository;
    @Autowired
    public PrioriteService(PrioriteRepository PrioriteRepository, ElementRepository elementRepository) {
        this.PrioriteRepository = PrioriteRepository;
        this.elementRepository = elementRepository;
    }

    public Priorite saveOrUpdatePriorite(Priorite Priorite) {
        return PrioriteRepository.save(Priorite);
    }

    public List<Priorite> getAllPriorites() {
        return PrioriteRepository.findAll();
    }

    public Optional<Priorite> getPrioriteById(Long id) {
        return PrioriteRepository.findById(id);
    }

    @Transactional
    public void deletePriorite(Long id) {
        // Additional checks can be performed here (like checking for dependencies before deleting)
        PrioriteRepository.deleteById(id);
    }
}
