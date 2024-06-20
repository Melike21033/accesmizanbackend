package com.mizanlabs.mr.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
@Entity
@Table(name = "tache")
public class Tache {
	   @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
		@Column(unique = true)
	    private String label;

	    public Tache() {
		super();
		// TODO Auto-generated constructor stub
	}
		public Tache(Long id, String label) {
		super();
		this.id = id;
		this.label = label;
	}
		public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	
}
