package com.mizanlabs.mr.repository;

import com.mizanlabs.mr.entities.User;
import com.mizanlabs.mr.entities.Verification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationRepository extends JpaRepository<Verification, Long> {
    Verification findByUserAndCode(User user, String code);
    void deleteByUser(User user); // Méthode pour supprimer les codes de vérification par utilisateur
    Verification findTopByUserOrderByExpirationDateDesc(User user);

}

