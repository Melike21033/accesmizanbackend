package com.mizanlabs.mr.entities;
import jakarta.persistence.*;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "contacts")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    public Contact() {
    }


    public Contact(String name, String telephone, String email, String address, Profession profession, String note, String whatsapp) {

        this.name = name;
        this.telephone = telephone;
        this.whatsapp = whatsapp;
        this.email = email;
        this.address = address;
        this.profession = profession;
        this.note = note;
  	this.whatsapp = whatsapp;
    }
    public String getWhatsapp() {
		return whatsapp;
	}

	public void setWhatsapp(String whatsapp) {
		this.whatsapp = whatsapp;
	}

	public void setId(Long id) {
		this.id = id;
	}
	@Column(name = "tel")
    private String telephone;


    public Long getId() {
        return id;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Profession getProfession() {
        return profession;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

    @Column(name = "email")
    private String email;

    @Column(name = "address")
    private String address;

    @ManyToOne
    @JoinColumn(name = "profession_id")
    private Profession profession;

    @Column(name = "note")
    private String note;


    @Column(name = "whatsapp") // Nouveau champ pour le numéro WhatsApp
    private String whatsapp;
    


}
