
package com.mizanlabs.mr.entities;

import jakarta.persistence.*;

@Entity
public class Situation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String labels;

    public Situation() {
    }

    public Situation(Long id, String labels) {
        this.labels = labels;
    }

    public Long getId() {
        return id;
    }

    public String getLabel() {
        return labels;
    }

    public void setLabel(String labels) {
        this.labels = labels;
    }
}
