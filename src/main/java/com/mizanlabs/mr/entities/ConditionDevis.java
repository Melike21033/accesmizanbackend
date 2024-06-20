package com.mizanlabs.mr.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "condition_devis")
public class ConditionDevis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "devis_id", nullable = false) // Utilisez @JoinColumn pour mapper la relation
    private Devis devis;

    @ManyToOne
    @JoinColumn(name = "conditionp_id", nullable = false) // Utilisez @JoinColumn pour mapper la relation
    private Conditionp conditionp;

    public ConditionDevis() {
    }

    public Long getId() {
        return id;
    }

    public Devis getDevis() {
        return devis;
    }

    public Conditionp getConditionp() {
        return conditionp;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDevis(Devis devis) {
        this.devis = devis;
    }

    public void setConditionp(Conditionp conditionp) {
        this.conditionp = conditionp;
    }
}
