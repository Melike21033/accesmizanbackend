package com.mizanlabs.mr.repository;

import com.mizanlabs.mr.entities.Facture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FactureRepository extends JpaRepository<Facture, Long> {
    // Vous pouvez ajouter des méthodes de requête personnalisées ici si nécessaire
}