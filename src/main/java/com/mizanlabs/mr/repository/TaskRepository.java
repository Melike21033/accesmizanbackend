package com.mizanlabs.mr.repository;

import com.mizanlabs.mr.entities.ElementDevis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.mizanlabs.mr.entities.Project;
import com.mizanlabs.mr.entities.Task;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByProject(Project project);
    Optional<Task> findByTaskName(String taskName);
    Optional<Task> findByTaskId(Long taskId);
    List<Task> findByProject_ProjectId(Long projectId);
    @Query("SELECT MAX(t.refTask) FROM Task t WHERE t.project.projectId = :projectId")
    String findMaxRefTaskByProjectId(@Param("projectId") Long projectId);
    @Query("SELECT t FROM Task t WHERE t.project = :project AND t.devis IS NULL")
    List<Task> findTasksByProjectAndDevisIsNull(@Param("project") Project project);

    @Query("SELECT t FROM Task t WHERE t.devis.id = :devisId")
    List<Task> findByDevisId(@Param("devisId") Long devisId);

    @Query("SELECT COUNT(t) > 0 FROM Task t WHERE t.taskName = :taskName AND t.project.projectId = :projectId")
    boolean existsByTaskNameAndProjectId(String taskName, Long projectId);
    @Modifying
    @Query("UPDATE Task t SET t.devis = NULL WHERE t.devis.id = :devisId")
    void dissociateDevisFromTasks(@Param("devisId") Long devisId);
    @Query("SELECT ed FROM Task t JOIN t.elementDevis ed WHERE t.taskId = :taskId")
    Set<ElementDevis> findElementDevisByTaskId(@Param("taskId") Long taskId);

    @Query("SELECT t.totalTask FROM Task t WHERE t.taskId = :taskId")
    Integer findTotalTaskById(@Param("taskId") Long taskId);
    @Query(value = "SELECT SUM(total_task) FROM task WHERE devis_id = :devisId", nativeQuery = true)
    Integer calculateTotalTaskForDevis(@Param("devisId") Long devisId);

    @Query("SELECT SUM(t.totalTask) FROM Task t WHERE t.devis.id = :devisId")
    Integer sumTotalTaskByDevisId(@Param("devisId") Long devisId);

    @Query("SELECT t.start, FUNCTION('DATEDIFF', t.deadline, t.start) FROM Task t")
    List<Object[]> findAllTaskDurations();
    @Query("SELECT t.status.label, COUNT(t) FROM Task t GROUP BY t.status.label")
    List<Object[]> getTaskStatusDistribution();

    @Query("SELECT t.status.label, COUNT(t) FROM Task t WHERE t.status.label = :status GROUP BY t.status.label")
    List<Object[]> getTaskStatusDistributionByStatus(@Param("status") String status);

    @Query("SELECT t.status.label, COUNT(t) FROM Task t WHERE t.start BETWEEN :startDate AND :endDate GROUP BY t.status.label")
    List<Object[]> getTaskStatusDistributionByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}