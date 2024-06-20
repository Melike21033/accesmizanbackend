package com.mizanlabs.mr.controller;

import com.mizanlabs.mr.entities.Client;
import com.mizanlabs.mr.entities.Contact;
import com.mizanlabs.mr.service.ClientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    // Create a new client
    @PostMapping
    public ResponseEntity<Client> createClient(@RequestBody Client client) {
        Client savedClient = clientService.createClient(client);
        return ResponseEntity.ok(savedClient);
    }
    // Get all clients
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @GetMapping
    public List<Client> getAllClients() {
        return clientService.getAllClients();
    }
    // Get a single client by ID
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        return clientService.getClientById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    // Update a client
    @PutMapping("/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable Long id, @RequestBody Client client) {
        return clientService.updateClient(id, client)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClient(@PathVariable Long id) {
        try {
            if (clientService.deleteClient(id)) {
                return ResponseEntity.ok().build();
            } else {
                // Si la méthode deleteClient retourne false, cela signifie que le client n'a pas été trouvé.
                return ResponseEntity.notFound().build();
            }
        } catch (DataIntegrityViolationException e) {
            // Cela capture les exceptions liées aux contraintes de la base de données, comme les clés étrangères.
            return ResponseEntity.badRequest().body("La suppression du client a échoué. Veuillez d'abord supprimer les projets, les contact associés au client.");
        } catch (Exception e) {
            // Capturer toute autre exception inattendue
            return ResponseEntity.internalServerError().body("Une erreur est survenue lors de la suppression du client.");
        }
    }

}
