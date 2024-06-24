package com.mizanlabs.mr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mizanlabs.mr.entities.Devis;
import com.mizanlabs.mr.entities.Project;

import jakarta.transaction.Transactional;

import java.util.List;
@Repository
public interface DevisRepository extends JpaRepository<Devis, Long> {
    List<Devis> findByProject(Project project);
    List<Devis> findByProjectProjectId(Long projectId);
    @Query("SELECT MAX(d.ref_devis) FROM Devis d WHERE d.annee = :year")
    String findMaxRefDevisByYear(@Param("year") String year);

    @Query(value = "SELECT MAX(devis_id) FROM devis", nativeQuery = true)
    Long findMaxDevisId();

    List<Devis> findByProject_ProjectId(Long projectId);


    @Query("SELECT SUM(t.totalTask) FROM Task t WHERE t.devis.id = :devisId")
    Integer findTotalTasksByDevisId(@Param("devisId") Long devisId);

    @Transactional
    @Modifying
    @Query("UPDATE Devis d SET d.montant = :montant WHERE d.id = :devisId")
    void updateDevisMontant(@Param("devisId") Long devisId, @Param("montant") Integer montant);
    @Query("SELECT d.status.label, COUNT(d) " +
            "FROM Devis d " +
            "GROUP BY d.status.label")
    List<Object[]> getDevisStatusDistribution();
}