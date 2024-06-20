
package com.mizanlabs.mr.service;

import com.mizanlabs.mr.entities.Situation;
import com.mizanlabs.mr.repository.SituationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Service
public class SituationService {
    private final SituationRepository situationRepository;
    @Autowired
    public SituationService(SituationRepository situationRepository) {
        this.situationRepository = situationRepository;
    }

    public Situation saveOrUpdateSituation(Situation Situation) {
        return situationRepository.save(Situation);
    }

    public List<Situation> getAllSituations() {
        return situationRepository.findAll();
    }

    public Optional<Situation> getSituationById(Long id) {
        return situationRepository.findById(id);
    }

    @Transactional
    public void deleteSituation(Long id) {
        // Additional checks can be performed here (like checking for dependencies before deleting)
        situationRepository.deleteById(id);
    }
}
