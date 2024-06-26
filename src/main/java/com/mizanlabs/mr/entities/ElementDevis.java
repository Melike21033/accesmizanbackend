package com.mizanlabs.mr.entities;

import java.util.Objects;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "element_devis")
public class ElementDevis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "element_note", length = 255)
    private String elementNote;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status;

    @Column(name = "prix_unitaire", length = 255)
    private Integer prix_unitaire;

    @Column(name = "montant", length = 255)
    private Integer montant;

    @Column(name = "unite", length = 255)
    private String unite;

    @Column(name = "refEdevis", length = 255, unique = true)
    private String refEdevis;

    @Column(name = "type", length = 255)
    private String type;

    @Column(name = "element_qty")
    private Integer elementQty;

    @Column(name = "nbreLots")
    private Integer nbreLots;

    @Column(name = "qteLots", length = 255)
    private String qteLots;

    @ManyToOne
    @JoinColumn(name = "task_id")
    @JsonBackReference(value = "task-elementDevis")
    private Task task;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "element_id")
    @JsonBackReference(value = "element-elementDevis")
    private Element element;

    @ManyToOne
    @JoinColumn(name = "facture_id")
    @JsonBackReference(value = "facture-elementDevis")
    private Facture facture;

    public ElementDevis() {
    }

    public ElementDevis(Long id, String name, String elementNote, Status status, Integer prix_unitaire, Integer montant, String unite, String refEdevis, String type, Integer elementQty, Integer nbreLots, String qteLots, Task task, Element element, Facture facture) {
        this.id = id;
        this.name = name;
        this.elementNote = elementNote;
        this.status = status;
        this.prix_unitaire = prix_unitaire;
        this.montant = montant;
        this.unite = unite;
        this.refEdevis = refEdevis;
        this.type = type;
        this.elementQty = elementQty;
        this.nbreLots = nbreLots;
        this.qteLots = qteLots;
        this.task = task;
        this.element = element;
        this.facture = facture;
    }

    public String getqteLots() {
        return qteLots;
    }

    public void setqteLots(String qteLots) {
        this.qteLots = qteLots;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElementDevis that = (ElementDevis) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
