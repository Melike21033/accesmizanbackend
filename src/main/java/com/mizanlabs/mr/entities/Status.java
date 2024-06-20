package com.mizanlabs.mr.entities;

import jakarta.persistence.*;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
@Entity
@Table(name = "Status")
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = false)
    private String label;


    @Column(name = "tableref")
    private String tableref;

    public Status() {
    }
    public Status(Long id, String label, String tableref) {
        this.id = id;
        this.label = label;
        this.tableref = tableref;
    }

    public Long getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }


    public void setLabel(String label) {
        this.label = label;
    }

    public String getTableref() {
        return tableref;
    }

    public void setTableref(String tableref) {
        this.tableref = tableref;
    }
}
