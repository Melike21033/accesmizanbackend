
package com.mizanlabs.mr.entities;

import jakarta.persistence.*;

@Entity
public class Situation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String label;

    public Situation() {
    }

    public Situation(Long id, String label) {
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
