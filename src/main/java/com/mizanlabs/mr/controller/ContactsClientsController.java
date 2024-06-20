package com.mizanlabs.mr.controller;

import com.mizanlabs.mr.entities.Contact;
import com.mizanlabs.mr.entities.ContactsClients;
import com.mizanlabs.mr.service.ContactsClientsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contacts-clients")
public class ContactsClientsController {

    private final ContactsClientsService contactsClientsService;

    @Autowired
    public ContactsClientsController(ContactsClientsService contactsClientsService) {
        this.contactsClientsService = contactsClientsService;
    }

    // Endpoint pour obtenir tous les contacts clients
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @GetMapping
    public ResponseEntity<List<ContactsClients>> getAllContactsClients() {
        List<ContactsClients> contactsClientsList = contactsClientsService.getAllContactsClients();
        return new ResponseEntity<>(contactsClientsList, HttpStatus.OK);
    }
    // Endpoint pour obtenir un contact client par son ID
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @GetMapping("/{id}")
    public ResponseEntity<ContactsClients> getContactClientById(@PathVariable Long id) {
        return contactsClientsService.getContactClientById(id)
                .map(contactClient -> ResponseEntity.ok().body(contactClient))
                .orElse(ResponseEntity.notFound().build());
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @GetMapping("/available-contacts/{clientId}")
    public ResponseEntity<List<Contact>> getAvailableContactsForClient(@PathVariable Long clientId) {
        List<Contact> availableContacts = contactsClientsService.getAvailableContactsForClient(clientId);
        return new ResponseEntity<>(availableContacts, HttpStatus.OK);
    }
    // Endpoint pour obtenir les contacts clients par ID client
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<ContactsClients>> getContactsClientsByClientId(@PathVariable Long clientId) {
        List<ContactsClients> contactsClientsList = contactsClientsService.getContactsClientsByClientId(clientId);
        return new ResponseEntity<>(contactsClientsList, HttpStatus.OK);
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @GetMapping("/principal/exists/{clientId}")
    public ResponseEntity<Boolean> checkIfPrincipalContactExistsForClient(@PathVariable Long clientId) {
        boolean exists = contactsClientsService.existsPrincipalContactForClient(clientId);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }

    // Endpoint pour ajouter un nouveau contact client
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @PostMapping
    public ResponseEntity<ContactsClients> addContactClient(@RequestBody ContactsClients contactsClients) {
        ContactsClients newContactClient = contactsClientsService.addContactClient(contactsClients);
        return new ResponseEntity<>(newContactClient, HttpStatus.CREATED);
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @PutMapping("/{id}")
    public ResponseEntity<ContactsClients> updateContactClient(@PathVariable Long id, @RequestBody ContactsClients updatedContactsClients) {
        return contactsClientsService.updateContactClient(id, updatedContactsClients)
                .map(updatedContactClient -> ResponseEntity.ok().body(updatedContactClient))
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint pour supprimer un contact client
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContactClient(@PathVariable Long id) {
        if (contactsClientsService.deleteContactClient(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
  
    
    
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @PutMapping("/contactProjectRelation")
    public ResponseEntity<?> addOrUpdateContactProjectRelation( @RequestParam Long contactId,@RequestParam Long clientId,@RequestParam Long projectId) {
        try {
            contactsClientsService.addOrUpdateContactProjectRelation(contactId, clientId, projectId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PatchMapping("/remove-project")
    public ResponseEntity<?> removeProjectId(@RequestParam Long contactId, @RequestParam Long projectId) {
        contactsClientsService.removeProjectIdByContactIdAndProjectId(contactId, projectId);
        return ResponseEntity.ok().body("Project ID removed successfully.");
    }
}
