//ElementEntity
package com.mizanlabs.mr.entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "element")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Element {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 255,unique = true)
    private String name;

    @Column(name = "note", length = 255)
    private String note;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private Type type;

    @OneToMany(mappedBy = "element", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<ElementDevis> elementDevis;

    public Type getType() {
        return type;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNote() {
        return note;
    }

    public Set<ElementDevis> getElementDevis() {
        return elementDevis;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setElementDevis(Set<ElementDevis> elementDevis) {
        this.elementDevis = elementDevis;
    }
}
