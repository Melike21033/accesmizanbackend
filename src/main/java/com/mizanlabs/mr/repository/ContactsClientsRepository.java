package com.mizanlabs.mr.repository;


import com.mizanlabs.mr.entities.Contact;

import com.mizanlabs.mr.entities.ContactsClients;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import com.mizanlabs.mr.entities.Contact;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
public interface ContactsClientsRepository extends JpaRepository<ContactsClients, Long> {

    // Méthode pour récupérer les contacts clients par ID client
    List<ContactsClients> findByProjectId(Long projectId);

    
    List<ContactsClients> findByClientId(Long clientId);
    boolean existsByIsPrincipal(Boolean isPrincipal);
    List<ContactsClients> findByContactIdAndClientId(Long contactId, Long clientId);
    @Query("SELECT c FROM Contact c WHERE c.id NOT IN (SELECT cc.contact.id FROM ContactsClients cc WHERE cc.client.id = :clientId) ORDER BY c.id DESC")
    List<Contact> findContactsNotLinkedToClient(@Param("clientId") Long clientId);

    boolean existsByClientIdAndIsPrincipal(Long clientId, Boolean isPrincipal);

    @Modifying
    @Transactional
    @Query("UPDATE ContactsClients cc SET cc.projectId = NULL WHERE cc.contact.id = :contactId AND cc.projectId = :projectId")
    void removeProjectIdByContactIdAndProjectId(@Param("contactId") Long contactId, @Param("projectId") Long projectId);
    List<ContactsClients> findByClientIdAndIsPrincipal(Long clientId, Boolean isPrincipal);

    
    
    List<ContactsClients> findByContactId(Long contactId);

}

