
package com.mizanlabs.mr.service;

import com.mizanlabs.mr.repository.ContactRepository;
import com.mizanlabs.mr.repository.ContactsClientsRepository;
import com.mizanlabs.mr.entities.Contact;
import com.mizanlabs.mr.entities.ContactsClients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ContactService {

    private final ContactRepository contactRepository;
    private final ContactsClientsRepository contactsClientsRepository;

    @Autowired
    public ContactService(ContactRepository contactRepository,ContactsClientsRepository contactsClientsRepository) {
        this.contactRepository = contactRepository;
        this.contactsClientsRepository = contactsClientsRepository;

    }

    // Renamed from findAllContacts to match the controller
    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }
    public List<Contact> getContactsNotLinkedToProject(Long projectId) {
        // Fetch all contacts linked to the project
        List<ContactsClients> linkedContacts = contactsClientsRepository.findByProjectId(projectId);

        // Extract contact IDs
        List<Long> linkedContactIds = linkedContacts.stream()
                .map(contactClient -> contactClient.getContact().getId())
                .collect(Collectors.toList());

        // Fetch all contacts and filter out the ones linked to the project
        return contactRepository.findAll().stream()
                .filter(contact -> !linkedContactIds.contains(contact.getId()))
                .collect(Collectors.toList());
    }
    // Renamed from findContactById to match the controller
    public Optional<Contact> getContactById(Long id) {
        return contactRepository.findById(id);
    }

    // Renamed from saveContact to match the controller
    public Contact createContact(Contact contact) {
        return contactRepository.save(contact);
    }

    // Renamed from updateContact and changed return type to Optional<Contact> to match the controller
    public Optional<Contact> updateContact(Long id, Contact contactDetails) {
        return contactRepository.findById(id).map(contact -> {
            contact.setName(contactDetails.getName());
            contact.setEmail(contactDetails.getEmail());
            contact.setProfession(contactDetails.getProfession());
            contact.setNote(contactDetails.getNote());
            contact.setTelephone(contactDetails.getTelephone());
            contact.setAddress(contactDetails.getAddress());
            // Set other fields...

    	    contact.setWhatsapp(contactDetails.getWhatsapp()); // Mettre à jour le numéro WhatsApp
            return contactRepository.save(contact);
        });
    }

    // Changed to return a boolean to match the controller's expectation
    public boolean deleteContact(Long id) {
        boolean exists = contactRepository.existsById(id);
        if (!exists) {
            return false;
        }
        contactRepository.deleteById(id);
        return true;
    }
    
    
    public List<Contact> getContactsByProjectId(Long projectId) {
        List<ContactsClients> contactsClientsList = contactsClientsRepository.findByProjectId(projectId);
        if (contactsClientsList.isEmpty()) return new ArrayList<>();

        List<Long> contactIds = contactsClientsList.stream()
                .map(cc -> cc.getContact().getId())
                .distinct()
                .collect(Collectors.toList());

        return contactRepository.findAllById(contactIds);
    }
}

