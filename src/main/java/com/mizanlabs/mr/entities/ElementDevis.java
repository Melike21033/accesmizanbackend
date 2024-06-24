//ElementDevis entity
package com.mizanlabs.mr.entities;
import java.util.Objects;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "element_devis")
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIgnoreProperties(ignoreUnknown = true)  // Ignore any unknown properties in JSON
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

    @Column(name = "refEdevis", length = 255,unique = true)
    private String refEdevis;

    @Column(name = "type", length = 255)
    private String type;

    @Column(name = "element_qty")
    private Integer elementQty;


    @Column(name = "nbreLots")
    private Integer nbreLots;

    @Column(name = "qteLots", length = 255)
    private String qteLots;

    // Assuming there's a ManyToOne relationship with Task
    @ManyToOne
    @JoinColumn(name = "task_id")
    @JsonBackReference
    private Task task;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "element_id")
    @JsonBackReference("element-elementDevis")
    private Element element;
//    // Additional relationships and methods can be added here as needed
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "project_id")
//    @JsonIgnore // This will ignore the project field during JSON serialization/deserialization
//    private Project project;

    public ElementDevis() {
    }

    public ElementDevis(Long id, String name, String elementNote, Status status, Integer prix_unitaire, Integer montant, String unite, String refEdevis, String type, Integer elementQty, Integer nbreLots, String qteLots, Task task, Element element, Project project) {
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
    }

    public Status getStatus() {
        return status;
    }

    public Integer getNbreLots() {
        return nbreLots;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setNbreLots(Integer nbreLots) {
        this.nbreLots = nbreLots;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public String getqteLots() {
        return qteLots;
    }

    public String getElementNote() {
        return elementNote;
    }

    public Integer getPrix_unitaire() {
        return prix_unitaire;
    }

    public Integer getMontant() {
        return montant;
    }


    @ManyToOne
    @JoinColumn(name = "facture_id")
    @JsonBackReference("facture-elementsDevis")
    private Facture facture;


    //    public Integer getnbreLots() {
//        return nbreLots;
//    }
    public String getUnite() {
        return unite;
    }

    public String getRefEdevis() {
        return refEdevis;
    }

    public String getType() {
        return type;
    }

    public Integer getElementQty() {
        return elementQty;
    }

    public Task getTask() {
        return task;
    }

    public Element getElement() {
        return element;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setElementNote(String elementNote) {
        this.elementNote = elementNote;
    }

    public void setPrix_unitaire(Integer prix_unitaire) {
        this.prix_unitaire = prix_unitaire;
    }

    public void setMontant(Integer montant) {
        this.montant = montant;
    }

    public void setUnite(String unite) {
        this.unite = unite;
    }

    public void setRefEdevis(String refEdevis) {
        this.refEdevis = refEdevis;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setElementQty(Integer elementQty) {
        this.elementQty = elementQty;
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