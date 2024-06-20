package com.mizanlabs.mr.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "Conditionp")
public class Conditionp {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String label;

    public Conditionp() {
    }

    public Conditionp(Long id, String label) {
        this.id = id;
        this.label = label;
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
}

