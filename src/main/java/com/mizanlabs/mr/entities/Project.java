package com.mizanlabs.mr.entities;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = "projects")
public class Project {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long projectId;

	@Column(name = "date_de_creation")
	private String creationDate;

	@Column(name = "project_mo", length = 255)
	private String projectMO;

	@Column(name = "project_moe", length = 255)
	private String projectMOE;

	@ManyToOne(optional = true)
	@JoinColumn(name = "bct_id", nullable = true)
	private BCT projectBCT;

	@Column(name = "project_localisation", length = 255)
	private String projectLocation;

	@Column(length = 255)
	private String title;

	@ManyToOne(optional = true)
	@JoinColumn(name = "status_id", nullable = true)
	private Status status;

	@Column(name = "annee")
	private String annee;

	@Column(name = "refProjet", length = 255, unique = true)
	private String refProjet;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "client_id")
	private Client client;

	@ManyToOne(optional = true)
	@JoinColumn(name = "situation_id")
	private Situation situation;

	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference(value = "project-task")
	private Set<Task> task;
	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference("project-devis")
	private Set<Devis> devis;

	public Project() {
		// Default constructor
	}

	public Project(Situation situation, Status status, Long projectId, String creationDate, String name, String projectMO,
				   String projectMOE, BCT projectBCT, String projectLocation, String title, String annee,
				   String refProjet, Client client, Set<Task> task) {
		this.projectId = projectId;
		this.creationDate = creationDate;
		this.projectMO = projectMO;
		this.projectMOE = projectMOE;
		this.projectBCT = projectBCT;
		this.projectLocation = projectLocation;
		this.title = title;
		this.annee = annee;
		this.refProjet = refProjet;
		this.client = client;
		this.task = task;
		this.situation = situation;
		this.status = status;
	}
}
