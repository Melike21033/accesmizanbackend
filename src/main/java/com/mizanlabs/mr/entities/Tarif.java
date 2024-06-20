//Tarif Entity
package com.mizanlabs.mr.entities;


import jakarta.persistence.*;
@Entity
@Table(name = "tarif")
public class Tarif {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "unite_id")
    private Unite unite;

    @ManyToOne
    @JoinColumn(name = "element_id")
    private Element element;

    @Column(name = "pritunit")
    private Integer pritunit;

    // Getters and setters
    @Column(name = "is_principal")
    private Boolean isPrincipal;

    public Boolean getPrincipal() {
        return isPrincipal;
    }

    public void setPrincipal(Boolean principal) {
        isPrincipal = principal;
    }

    public Integer getPritunit() {
        return pritunit;
    }

    public Tarif() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Tarif(Long id, Unite unite, Element element, Integer pritunit,Boolean isPrincipal) {
		super();
		this.id = id;
		this.unite = unite;
		this.element = element;
		this.pritunit = pritunit;
        this.isPrincipal = isPrincipal;
	}

	public void setPritunit(Integer pritunit) {
        this.pritunit = pritunit;
    }

    public Long getId() {
        return id;
    }

    public Unite getUnite() {
        return unite;
    }

    public void setUnite(Unite unite) {
        this.unite = unite;
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
