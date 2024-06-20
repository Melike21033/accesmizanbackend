package com.mizanlabs.mr.repository;

import com.mizanlabs.mr.entities.ConditionDevis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConditionDevisRepository extends JpaRepository<ConditionDevis, Long> {
    List<ConditionDevis> findByDevis_DevisId(Long devisId);
}

