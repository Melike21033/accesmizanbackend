package com.mizanlabs.mr.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Data
@Entity
@Table(name = "ligne_devis") // Modifier pour utiliser underscore au lieu de tiret
public class LigneDevis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "element_note", length = 255)
    private String elementNote;

    @Column(name = "element_qty")
    private Integer elementQty;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status Status;

    @Column(name = "client_name")
    private String clientName; // Modifié pour respecter camelCase

    @Column(name = "item_name", length = 255)
    private String itemName; // Modifié pour respecter camelCase

    @Column(name = "item_price")
    private Integer itemPrice; // Modifié pour respecter camelCase

    

    @Column(name = "montant")
    private Integer montant; // Modifié pour respecter camelCase

    @Column(name = "item_type", length = 255)
    private String itemType; // Modifié pour respecter camelCase

    @Column(name = "unit", length = 255)
    private String itemUnit; // Modifié pour respecter camelCase

    @Column(length = 255)
    private String projectName; // Modifié pour respecter camelCase

    @Column(name = "task_name", length = 255)
    private String taskName; // Modifié pour respecter camelCase
    
    @Column(name = "taskId", length = 255)
    private Long taskId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "devis_id")
    @JsonBackReference("devis-ligneDevis")
    private Devis devis;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getElementNote() {
        return elementNote;
    }

    public Integer getElementQty() {
        return elementQty;
    }

    public String getClientName() {
        return clientName;
    }

    public String getItemName() {
        return itemName;
    }

    public Integer getItemPrice() {
        return itemPrice;
    }

    public Integer getMontant() {
        return montant;
    }

    public String getItemType() {
        return itemType;
    }

    public String getItemUnit() {
        return itemUnit;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getTaskName() {
        return taskName;
    }

    public Long getTaskId() {
        return taskId;
    }

    public Devis getDevis() {
        return devis;
    }

    public void setElementNote(String elementNote) {
        this.elementNote = elementNote;
    }

    public void setElementQty(Integer elementQty) {
        this.elementQty = elementQty;
    }

    public void setStatus(com.mizanlabs.mr.entities.Status status) {
        Status = status;
    }

    public com.mizanlabs.mr.entities.Status getStatus() {
        return Status;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemPrice(Integer itemPrice) {
        this.itemPrice = itemPrice;
    }

    public void setMontant(Integer montant) {
        this.montant = montant;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public void setItemUnit(String itemUnit) {
        this.itemUnit = itemUnit;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public void setDevis(Devis devis) {
        this.devis = devis;
    }
}
