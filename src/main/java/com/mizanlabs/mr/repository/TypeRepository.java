package com.mizanlabs.mr.repository;

import com.mizanlabs.mr.entities.Type;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TypeRepository extends JpaRepository<Type, Long> {
    List<Type> findByLabel(String label);

}