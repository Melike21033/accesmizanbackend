package com.mizanlabs.mr.entities;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "devis")
public class Devis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long devisId;

    @Column(name = "creationDate")
    private String creationDate;

    @Column(name = "creationDatedemande")
    private String creationDatedemande;

    @Column(name = "montant", nullable = false)
    private Integer montant = 0;

    @Column(name = "montant_remise", nullable = false)
    private Integer montantRemise = 0;

    @Column(name = "montant_tva", nullable = false)
    private Integer montantTva = 0;

    @Column(name = "devis_discount")
    private Integer discount;

    @Column(name = "devis_discountp")
    private Integer discountp;

    @Column(name = "TVA")
    private Integer TVA;

    @Column(name = "devis_note", length = 255)
    private String note;

    @Column(name = "tva_present")
    private Boolean tva_present;

    @ManyToOne(optional = true)
    @JoinColumn(name = "status_id", nullable = true)
    private Status status;

    @Column(name = "ref_devis", length = 25)
    private String ref_devis;

    @Column(name = "annee")
    private String annee;

    @ManyToOne
    @JoinColumn(name = "project_id")
    @JsonBackReference(value = "project-devis")
    private Project project;

    @OneToMany(mappedBy = "devis", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "devis-task")
    private List<Task> tasks;
    
    @OneToMany(mappedBy = "devis", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "devis-facture")
    private List<Facture> factures;

    @Column(name = "MP1")
    private String MP1;

    @Column(name = "MP2")
    private String MP2;

    @Column(name = "MP3")
    private String MP3;

    @Column(name = "MP4")
    private String MP4;

    @Column(name = "MP5")
    private String MP5;

    @Column(name = "PMP1")
    private String PMP1;

    @Column(name = "PMP2")
    private String PMP2;

    @Column(name = "PMP3")
    private String PMP3;

    @Column(name = "PMP4")
    private String PMP4;

    @Column(name = "PMP5")
    private String PMP5;

    @Column(name = "datedemarage")
    private String datedemarage;

    @Column(name = "remiserapport")
    private String remiserapport;
}
