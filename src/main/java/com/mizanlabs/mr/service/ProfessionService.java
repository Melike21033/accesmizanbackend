
package com.mizanlabs.mr.service;

import com.mizanlabs.mr.entities.Profession;
import com.mizanlabs.mr.repository.ProfessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Service
public class ProfessionService {
    private final ProfessionRepository ProfessionRepository;
    @Autowired
    public ProfessionService(ProfessionRepository ProfessionRepository) {
        this.ProfessionRepository = ProfessionRepository;
    }

    public Profession saveOrUpdateProfession(Profession Profession) {
        return ProfessionRepository.save(Profession);
    }

    public List<Profession> getAllProfessions() {
        return ProfessionRepository.findAll();
    }

    public Optional<Profession> getProfessionById(Long id) {
        return ProfessionRepository.findById(id);
    }

    @Transactional
    public void deleteProfession(Long id) {
        // Additional checks can be performed here (like checking for dependencies before deleting)
        ProfessionRepository.deleteById(id);
    }
}
