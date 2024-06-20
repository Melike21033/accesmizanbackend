 package com.mizanlabs.mr.service;

import com.mizanlabs.mr.entities.Client;
import com.mizanlabs.mr.entities.Contact;
import com.mizanlabs.mr.entities.ContactsClients;
import com.mizanlabs.mr.repository.ClientRepository;
import com.mizanlabs.mr.repository.ContactRepository;
import com.mizanlabs.mr.repository.ContactsClientsRepository;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final ContactRepository contactRepository;
    private final ContactsClientsRepository contactsClientsRepository;
    @Autowired
    public ClientService(ClientRepository clientRepository,ContactRepository contactRepository,ContactsClientsRepository contactsClientsRepository) {
        this.clientRepository = clientRepository;
        this.contactRepository = contactRepository;
        this.contactsClientsRepository = contactsClientsRepository;
    }

    // Rename this method to match the controller
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }
 
    // Rename this method to match the controller
    public Optional<Client> getClientById(Long id) {
        return clientRepository.findById(id);
    }

    public boolean existsById(Long id) {
        return clientRepository.existsById(id);
    }

    // Rename this method to match the controller
    public Client createClient(Client client) {
        return clientRepository.save(client);
    }

    // Rename this method to match the controller and change the return type to Optional<Client>
    public Optional<Client> updateClient(Long id, Client clientDetails) {
        return clientRepository.findById(id).map(client -> {
            client.setName(clientDetails.getName());
            client.setEmail(clientDetails.getEmail());
            client.setTelephone(clientDetails.getTelephone());
            client.setAddress(clientDetails.getAddress());
            client.setStatus(clientDetails.getStatus());
            client.setNote(clientDetails.getNote());

            // Set other fields...
            return clientRepository.save(client);
        });
    }
    



    // Change this method to return boolean to match the controller expectation
    public boolean deleteClient(Long id) {
        boolean exists = clientRepository.existsById(id);
        if (!exists) {
            return false;
        }
        clientRepository.deleteById(id);
        return true;
    }
    


}
