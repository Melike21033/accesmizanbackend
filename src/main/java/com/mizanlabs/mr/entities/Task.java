//Task entity
package com.mizanlabs.mr.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "task")
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "taskId")

public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;
    @Column(name = "total_task", nullable = false)
    private Integer totalTask = 0;

    @Column(name = "task_deadline")
    private LocalDate deadline;


    @Column(name = "task_name", length = 255,unique = false)
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
    @Column(name = "refTask", length = 255,unique = true)
    private String refTask;
    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    @JsonBackReference
    private Project project;

    @OneToMany(mappedBy = "task")
    @JsonManagedReference
    private Set<ElementDevis> elementDevis;


    @ManyToOne


    @JoinColumn(name = "devis_id") // Assurez-vous que le nom de la colonne correspond à votre base de données
    @JsonBackReference("devis-task")
    private Devis devis;

    public void setRefTask(String refTask) {
        this.refTask = refTask;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setNote(String note) {
        this.note = note;
    }



    public void setProject(Project project) {
        this.project = project;
    }

    public void setDevis(Devis devis) {
        this.devis = devis;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }



    public Long getTaskId() {
        return taskId;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getNote() {
        return note;
    }

    public LocalDate getStart() {
        return start;
    }

    public Priorite getPriority() {
        return priority;
    }

    public Status getStatus() {
        return status;
    }

    public void setPriority(Priorite priority) {
        this.priority = priority;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getRefTask() {
        return refTask;
    }

    public Set<ElementDevis> getElementDevis() {
        return elementDevis;
    }

    public Devis getDevis() {
        return devis;
    }

    public Project getProject() {
        return project;
    }


    public Task(Integer totalTask,Long taskId, LocalDate deadline, Integer montant, String taskName, String note, Priorite priority, LocalDate start,
                Status status, String refTask, Project project, Set<ElementDevis> elementDevis, Devis devis) {
        super();
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
        this.totalTask = totalTask;

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
}