package com.mizanlabs.mr.entities;

import jakarta.persistence.*;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;


@Entity
@Table(name = "clients")
@JsonIgnoreProperties({"projects", "factures"})
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;
    @ManyToOne(optional = true)
    @JoinColumn(name = "status_id", nullable = true)
    private Status status;

    @Column(name = "tel")
    private String telephone;

    @Column(name = "email")
    private String email;

    @Column(name = "address")
    private String address;

                    @Column(name = "note")
                    private String note;

                    @OneToMany(mappedBy = "client")
                    @JsonIgnoreProperties("Projects")

                    private Set<Project> projects;


                    
//                    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
//                    private Set<Facture> factures; // Liste des factures du client

                    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
                    private Set<Facture> factures;

                    
                    // getters and setters are omitted for brevity

    // No-args constructor
    public Client() {
    }

    // All-args constructor

    public Client(Long id, String name, Status status, String telephone, String email, String address, String note, Set<Project> projects, Set<Facture> factures) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.telephone = telephone;
        this.email = email;
        this.address = address;
        this.note = note;
        this.projects = projects;
        this.factures = factures;
    }

    public Long getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
    
    public Set<Facture> getFactures() {
        return factures;
    }

    public void setFactures(Set<Facture> factures) {
        this.factures = factures;
    } 


}


