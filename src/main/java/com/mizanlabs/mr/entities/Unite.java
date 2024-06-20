//Unite Entity
package com.mizanlabs.mr.entities;

import jakarta.persistence.Column;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Unite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true) // Cette ligne garantit que chaque valeur de 'unite' sera unique dans la base de données.

    private String unite;

    // Constructors, getters, setters, and other methods

    public Unite() {
    }

    public Unite(String unite) {
        this.unite = unite;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUnite() {
        return unite;
    }

    public void setUnite(String unite) {
        this.unite = unite;
    }

    @Override
    public String toString() {
        return "Unite{" +
                "id=" + id +
                ", unite='" + unite + '\'' +
                '}';
    }
}
