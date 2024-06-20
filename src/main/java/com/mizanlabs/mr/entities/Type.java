package com.mizanlabs.mr.entities;


import jakarta.persistence.*;

@Entity
public class Type {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_type;
    @Column(unique = true)
    private String label;

    public Type() {
    }

    public Type(String label) {
        this.label = label;
    }

    public Long getId_type() {
        return id_type;
    }

    public void setId_type(Long id_type) {
        this.id_type = id_type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "Type{" +
                "id_type=" + id_type +
                ", label='" + label + '\'' +
                '}';
    }
}
