package com.mizanlabs.mr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mizanlabs.mr.entities.ElementDevis;
import com.mizanlabs.mr.entities.Task;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.mizanlabs.mr.entities.Type;
@Repository
public interface ElementDevisRepository extends JpaRepository<ElementDevis, Long> {
    List<ElementDevis> findByTask(Task task);
    List<ElementDevis> findByTask_TaskId(Long taskId);
    @Query("SELECT MAX(e.refEdevis) FROM ElementDevis e WHERE e.task.id = :taskId")
    String findMaxRefEdevisByTaskId(@Param("taskId") Long taskId);
    @Query("SELECT e.refEdevis FROM ElementDevis e WHERE e.id = :id")
    String findRefEdevisById(@Param("id") Long id);
    @Query("SELECT ed FROM ElementDevis ed JOIN ed.task t JOIN ed.element e WHERE t.taskName = :taskName AND (:type IS NULL OR e.type = :type)")
    List<ElementDevis> findByTaskNameAndType(@Param("taskName") String taskName, @Param("type") Type type);

    @Query("SELECT ed FROM ElementDevis ed JOIN ed.element e JOIN e.type t WHERE ed.task.taskName = :taskName ORDER BY t.label ASC")
    List<ElementDevis> findByTaskNameSortedByType(@Param("taskName") String taskName);


    @Query("SELECT COUNT(e) FROM ElementDevis e WHERE e.type = :typeLabel")
    int countByType(@Param("typeLabel") String typeLabel);

    @Query("SELECT ed FROM ElementDevis ed WHERE ed.type = :typeLabel")
    List<ElementDevis> findByTypeLabel(@Param("typeLabel") String typeLabel);

    @Query("SELECT SUM(ed.montant) FROM ElementDevis ed WHERE ed.task.taskId = :taskId AND ed.status.label <> 'PM'")
    Integer sumMontantByTaskId(@Param("taskId") Long taskId);

    @Query("SELECT e.status.label, COUNT(e) " +
            "FROM ElementDevis e " +
            "GROUP BY e.status.label")
    List<Object[]> getElementDevisStatusDistribution();
}