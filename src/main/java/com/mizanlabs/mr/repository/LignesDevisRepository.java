package com.mizanlabs.mr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mizanlabs.mr.entities.LigneDevis;

@Repository
public interface LignesDevisRepository extends JpaRepository<LigneDevis, Long> {
    @Query("SELECT DISTINCT ld.taskName FROM LigneDevis ld WHERE ld.projectName = :projectName")
	    List<String> findTaskNamesByProjectName(String projectName);
    @Query("SELECT  ld FROM LigneDevis ld WHERE ld.taskId = :taskId")
    List<LigneDevis> findDistinctByTaskId(@Param("taskId") Long taskId);

    }
