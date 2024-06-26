package com.mizanlabs.mr.entities;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Data
@Entity
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;

    @Column(name = "total_task", nullable = false)
    private Integer totalTask = 0;

    @Column(name = "task_deadline")
    private LocalDate deadline;

    @Column(name = "task_name", length = 255, unique = false)
    private String taskName;

    @Column(name = "task_note", length = 255)
    private String note;

    @ManyToOne
    @JoinColumn(name = "priority_id")
    private Priorite priority;

    @Column(name = "task_start")
    private LocalDate start;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status;

    @Column(name = "refTask", length = 255, unique = true)
    private String refTask;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    @JsonBackReference(value = "project-task")
    private Project project;

    @OneToMany(mappedBy = "task")
    @JsonManagedReference("task-elementDevis")
    private Set<ElementDevis> elementDevis;

    @ManyToOne
    @JoinColumn(name = "devis_id")
    @JsonBackReference(value = "devis-task")
    private Devis devis;


    public Task(Integer totalTask, Long taskId, LocalDate deadline, Integer montant, String taskName, String note,
                Priorite priority, LocalDate start, Status status, String refTask, Project project,
                Set<ElementDevis> elementDevis, Devis devis) {
        this.totalTask = totalTask;
        this.taskId = taskId;
        this.deadline = deadline;
        this.taskName = taskName;
        this.note = note;
        this.priority = priority;
        this.start = start;
        this.status = status;
        this.refTask = refTask;
        this.project = project;
        this.elementDevis = elementDevis;
        this.devis = devis;
    }

    public Integer getTotalTask() {
        return totalTask != null ? totalTask : 0;
    }


    public void setTotalTask(Integer totalTask) {
        this.totalTask = totalTask;
    }

    public Task() {
        super();
        this.totalTask = 0; // Initialise totalTask à 0
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(taskId, task.taskId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskId);
    }
}

