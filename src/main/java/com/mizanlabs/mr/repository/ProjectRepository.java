package com.mizanlabs.mr.repository;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mizanlabs.mr.entities.Client;
import com.mizanlabs.mr.entities.Project;

import java.util.List;
import java.util.Optional;
@Repository

public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("SELECT MAX(p.refProjet) FROM Project p WHERE p.annee = :year")
    String findMaxRefProjetByYear(@Param("year") String year);
    @Query("SELECT p.refProjet FROM Project p WHERE p.projectId = :projectId")
    String findRefProjetByProjectId(@Param("projectId") Long projectId);

    @Query("SELECT p.client FROM Project p WHERE p.projectId = :projectId")
    Client findClientByProjectId(@Param("projectId") Long projectId);
    
    Optional<Project> findByTitle(String title);
    
    Optional<Project> findByRefProjet(String refProjet);
    
    List<Project> findByClientId(Long clientId);
    Project findByProjectId(Long projectId);
    @Query("SELECT p.status.label, COUNT(p) " +
            "FROM Project p " +
            "GROUP BY p.status.label")
    List<Object[]> getProjetStatusDistribution();
}