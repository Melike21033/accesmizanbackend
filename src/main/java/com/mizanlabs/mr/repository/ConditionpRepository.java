package com.mizanlabs.mr.repository;

import com.mizanlabs.mr.entities.Conditionp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConditionpRepository extends JpaRepository<Conditionp, Long> {
}
