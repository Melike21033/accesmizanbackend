package com.mizanlabs.mr.service;

import com.mizanlabs.mr.entities.Conditionp;
import com.mizanlabs.mr.repository.ConditionpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ConditionpService {
    private final ConditionpRepository conditionpRepository;

    @Autowired
    public ConditionpService(ConditionpRepository conditionpRepository) {
        this.conditionpRepository = conditionpRepository;
    }

    public Conditionp saveOrUpdateConditionp(Conditionp conditionp) {
        return conditionpRepository.save(conditionp);
    }

    public List<Conditionp> getAllConditionps() {
        return conditionpRepository.findAll();
    }

    public Optional<Conditionp> getConditionpById(Long id) {
        return conditionpRepository.findById(id);
    }

    @Transactional
    public void deleteConditionp(Long id) {
        // Des vérifications supplémentaires peuvent être effectuées ici (comme vérifier les dépendances avant de supprimer)
        conditionpRepository.deleteById(id);
    }
}
