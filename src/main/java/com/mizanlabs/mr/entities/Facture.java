package com.mizanlabs.mr.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "factures")
public class Facture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_creation", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;

    @Column(name = "montant_total", nullable = false)
    private Double montantTotal;

    @Column(name = "montant_residuel", nullable = false)
    private Double montantResiduel;

    @Column(name = "modalite")
    private String modalite;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "devis_id", nullable = false)
    @JsonBackReference(value = "devis-facture")
    private Devis devis;

    @OneToMany(mappedBy = "facture", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ElementDevis> elements;

    // Constructeurs
    public Facture(Long id, Date dateCreation, Double montantTotal, Double montantResiduel, Client client, Devis devis, List<ElementDevis> elements, String modalite) {
        this.id = id;
        this.dateCreation = dateCreation;
        this.montantTotal = montantTotal;
        this.montantResiduel = montantResiduel;
        this.client = client;
        this.devis = devis;
        this.elements = elements;
        this.modalite = modalite;
    }

    public Facture() {}

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Double getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(Double montantTotal) {
        this.montantTotal = montantTotal;
    }

    public Double getMontantResiduel() {
        return montantResiduel;
    }

    public void setMontantResiduel(Double montantResiduel) {
        this.montantResiduel = montantResiduel;
    }

    public String getModalite() {
        return modalite;
    }

    public void setModalite(String modalite) {
        this.modalite = modalite;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Devis getDevis() {
        return devis;
    }

    public void setDevis(Devis devis) {
        this.devis = devis;
    }

    public List<ElementDevis> getElements() {
        return elements;
    }

    public void setElements(List<ElementDevis> elements) {
        this.elements = elements;
    }
}
