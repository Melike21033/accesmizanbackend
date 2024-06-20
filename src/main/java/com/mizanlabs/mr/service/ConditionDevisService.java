package com.mizanlabs.mr.service;

import com.mizanlabs.mr.entities.ConditionDevis;
import com.mizanlabs.mr.repository.ConditionDevisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConditionDevisService {

    @Autowired
    private ConditionDevisRepository conditionDevisRepository;

    public List<ConditionDevis> getAllConditionDevis() {
        return conditionDevisRepository.findAll();
    }

    public Optional<ConditionDevis> getConditionDevisById(Long id) {
        return conditionDevisRepository.findById(id);
    }
    public List<ConditionDevis> getConditionsByDevisId(Long devisId) {
        return conditionDevisRepository.findByDevis_DevisId(devisId);
    }
    public ConditionDevis saveConditionDevis(ConditionDevis conditionDevis) {
        return conditionDevisRepository.save(conditionDevis);
    }

    public void deleteConditionDevis(Long id) {
        conditionDevisRepository.deleteById(id);
    }
}
