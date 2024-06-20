package com.mizanlabs.mr.service;

import com.mizanlabs.mr.entities.Contact;
import com.mizanlabs.mr.entities.ContactsClients;
import com.mizanlabs.mr.repository.ClientRepository;
import com.mizanlabs.mr.repository.ContactRepository;
import com.mizanlabs.mr.repository.ContactsClientsRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactsClientsService {

    private final ContactsClientsRepository contactsClientsRepository;
    private final ContactRepository contactRepository;
    private final ClientRepository clientRepository ;

    
    @Autowired
    public ContactsClientsService(ContactsClientsRepository contactsClientsRepository,ContactRepository contactRepository,ClientRepository clientRepository) {
        this.contactsClientsRepository = contactsClientsRepository;
        this.contactRepository = contactRepository;
        this.clientRepository = clientRepository;

    }

    // Méthode pour obtenir tous les contacts clients
    public List<ContactsClients> getAllContactsClients() {
        return contactsClientsRepository.findAll();
    }

    // Méthode pour obtenir les contacts clients par ID client
    public List<ContactsClients> getContactsClientsByClientId(Long clientId) {
        return contactsClientsRepository.findByClientId(clientId);
    }
    public boolean existsPrincipalContactForClient(Long clientId) {
        return contactsClientsRepository.existsByClientIdAndIsPrincipal(clientId, true);
    }
    public List<Contact> getAvailableContactsForClient(Long clientId) {
        return contactsClientsRepository.findContactsNotLinkedToClient(clientId);
    }
    // Méthode pour ajouter un contact client
    public ContactsClients addContactClient(ContactsClients contactsClients) {
        if (Boolean.TRUE.equals(contactsClients.getIsPrincipal())) {
            List<ContactsClients> existingContacts = contactsClientsRepository.findByClientIdAndIsPrincipal(contactsClients.getClient().getId(), true);
            existingContacts.forEach(contact -> {
                contact.setIsPrincipal(false);
                contactsClientsRepository.save(contact);
            });
        }
        return contactsClientsRepository.save(contactsClients);
    }
    public Optional<ContactsClients> updateContactClient(Long id, ContactsClients updatedContactsClients) {
        return contactsClientsRepository.findById(id).map(existingContact -> {
            if (Boolean.TRUE.equals(updatedContactsClients.getIsPrincipal())) {
                List<ContactsClients> existingContacts = contactsClientsRepository.findByClientIdAndIsPrincipal(updatedContactsClients.getClient().getId(), true);
                existingContacts.forEach(contact -> {
                    if (!contact.getId().equals(id)) {
                        contact.setIsPrincipal(false);
                        contactsClientsRepository.save(contact);
                    }
                });
            }
            existingContact.setIsPrincipal(updatedContactsClients.getIsPrincipal());
            existingContact.setContact(updatedContactsClients.getContact());
            existingContact.setClient(updatedContactsClients.getClient());
            return contactsClientsRepository.save(existingContact);
        });
    }

    // Méthode pour supprimer un contact client
    public boolean deleteContactClient(Long id) {
        boolean exists = contactsClientsRepository.existsById(id);
        if (!exists) {
            return false;}
        contactsClientsRepository.deleteById(id);
        return true;
    }
    // Méthode pour obtenir un contact client par son ID
    public Optional<ContactsClients> getContactClientById(Long id) {
        return contactsClientsRepository.findById(id);
    }
    @Transactional
    public void addOrUpdateContactProjectRelation(Long contactId, Long clientId, Long projectId) {
        List<ContactsClients> relations = contactsClientsRepository.findByContactIdAndClientId(contactId, clientId);

        // Aucun enregistrement trouvé : insérer un nouvel enregistrement.
        if (relations.isEmpty()) {
            ContactsClients newRelation = new ContactsClients();
            newRelation.setContact(contactRepository.findById(contactId).orElseThrow(() -> new RuntimeException("Contact not found")));
            newRelation.setClient(clientRepository.findById(clientId).orElseThrow(() -> new RuntimeException("Client not found")));
            newRelation.setProjectId(projectId);
            contactsClientsRepository.save(newRelation);
            return;
        }

        boolean updated = false;
        for (ContactsClients relation : relations) {
            // Cas où l'enregistrement existe mais sans projet associé.
            if (relation.getProjectId() == null) {
                relation.setProjectId(projectId);
                relation.setIsPrincipal(false);

                contactsClientsRepository.save(relation);
                updated = true;
                break; // Supposons que vous ne voulez mettre à jour qu'un seul enregistrement.
            }
        }

        // Si aucun enregistrement n'a été mis à jour (tous ont un projectId), insérer un nouvel enregistrement.
        if (!updated) {
            ContactsClients newRelation = new ContactsClients();
            newRelation.setContact(contactRepository.findById(contactId).orElseThrow(() -> new RuntimeException("Contact not found")));
            newRelation.setClient(clientRepository.findById(clientId).orElseThrow(() -> new RuntimeException("Client not found")));
            newRelation.setProjectId(projectId);
            newRelation.setIsPrincipal(false);
            contactsClientsRepository.save(newRelation);
        }
    }
    
    
    public void removeProjectIdByContactIdAndProjectId(Long contactId, Long projectId) {
        contactsClientsRepository.removeProjectIdByContactIdAndProjectId(contactId, projectId);
    }
}
