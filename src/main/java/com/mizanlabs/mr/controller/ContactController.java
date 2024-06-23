package com.mizanlabs.mr.controller;

import com.mizanlabs.mr.entities.Contact;
import com.mizanlabs.mr.service.ContactService;
import com.mizanlabs.mr.service.ContactsClientsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contacts")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class ContactController {

    private final ContactService contactService;
    private final ContactsClientsService contactsClientsService;

    @Autowired
    public ContactController(ContactService contactService,ContactsClientsService contactsClientsService) {
        this.contactService = contactService;
        this.contactsClientsService = contactsClientsService;

    }

    // Create a new contact
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    @PostMapping
    public ResponseEntity<Contact> createContact(@RequestBody Contact contact) {
        Contact savedContact = contactService.createContact(contact);
        return ResponseEntity.ok(savedContact);
    }

    // Get all contacts
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    @GetMapping
    public List<Contact> getAllContacts() {
        return contactService.getAllContacts();
    }

    // Get a single contact by ID
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @GetMapping("/{id}")
    public ResponseEntity<Contact> getContactById(@PathVariable Long id) {
        return contactService.getContactById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @GetMapping("/notLinked/{projectId}")
    public ResponseEntity<List<Contact>> getContactsNotLinkedToProject(@PathVariable Long projectId) {
        List<Contact> contacts = contactService.getContactsNotLinkedToProject(projectId);
        return ResponseEntity.ok(contacts);
    }
    // Update a contact
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    @PutMapping("/{id}")
    public ResponseEntity<Contact> updateContact(@PathVariable Long id, @RequestBody Contact contact) {
        return contactService.updateContact(id, contact)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete a contact
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteContact(@PathVariable Long id) {
        try {
            if (contactService.deleteContact(id)) {
                return ResponseEntity.ok().build();
            } else {
                // Si la méthode deleteClient retourne false, cela signifie que le client n'a pas été trouvé.
                return ResponseEntity.notFound().build();
            }
        } catch (DataIntegrityViolationException e) {
            // Cela capture les exceptions liées aux contraintes de la base de données, comme les clés étrangères.
            return ResponseEntity.badRequest().body("La suppression du contact a échoué. Veuillez d'abord supprimer les projets, les clients associés au contact.");
        } catch (Exception e) {
            // Capturer toute autre exception inattendue
            return ResponseEntity.internalServerError().body("Une erreur est survenue lors de la suppression du contact.");
        }
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    @GetMapping("/byProject/{projectId}")
    public ResponseEntity<List<Contact>> getContactsByProjectId(@PathVariable Long projectId) {
        List<Contact> contacts = contactService.getContactsByProjectId(projectId);
        if (contacts.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(contacts);
    }
    
}
