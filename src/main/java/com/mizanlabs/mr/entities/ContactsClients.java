package com.mizanlabs.mr.entities;


import jakarta.persistence.*;
@Entity
@Table(name = "contacts_clients")
public class ContactsClients {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "contact_id", nullable = false) // Utilisez @JoinColumn pour mapper la relation
    private Contact contact;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false) // Utilisez @JoinColumn pour mapper la relation
    private Client client;

    @Column(name = "is_principal")
    private Boolean isPrincipal;
 
    @Column(name = "project_id")
    private Long projectId; // Supposons que l'ID du projet est de type Long


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Boolean getIsPrincipal() {
        return isPrincipal;
    }

    public ContactsClients() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ContactsClients(Long id, Contact contact, Client client, Boolean isPrincipal, Long projectId) {
		super();
		this.id = id;
		this.contact = contact;
		this.client = client;
		this.isPrincipal = isPrincipal;
		this.projectId = projectId;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public void setIsPrincipal(Boolean isPrincipal) {
        this.isPrincipal = isPrincipal;
    }

}
