package com.mizanlabs.mr.service;

import com.mizanlabs.mr.entities.User;
import com.mizanlabs.mr.entities.Verification;
import com.mizanlabs.mr.repository.UserRepository;
import com.mizanlabs.mr.repository.VerificationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationRepository verificationRepository;

    @Autowired
    private EmailService emailService;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void createUser(String name, String email, String role) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setRole(role);
        userRepository.save(user);
    }

    public void sendVerificationCode(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            String code = generateVerificationCode();
            Verification verificationCode = new Verification();
            verificationCode.setUser(user);
            verificationCode.setCode(code);
            verificationCode.setExpirationDate(LocalDateTime.now().plusMinutes(3));
            verificationRepository.save(verificationCode);

            emailService.sendVerificationEmail(email, code);
        }
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = 10000000 + random.nextInt(90000000); // Génère un nombre à 8 chiffres
        return String.valueOf(code);
    }

    public boolean verifyCode(String email, String code) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return false;
        }

        Verification verificationCode = verificationRepository.findByUserAndCode(user, code);
        if (verificationCode == null || verificationCode.getExpirationDate().isBefore(LocalDateTime.now())) {
            return false;
        }

        return true;
    }
    @Transactional
    public void setPassword(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.setPassword(passwordEncoder.encode(password));
            user.setIsActive(true);
            userRepository.save(user);

            // Supprimer le code de vérification associé à cet utilisateur
            verificationRepository.deleteByUser(user);
        }
    }
    public void updateUser(Long id, String name, String email, String role) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setName(name);
        user.setEmail(email);
        user.setRole(role);
        userRepository.save(user);
    }
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    public boolean emailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }
    public LocalDateTime getCodeExpirationTime(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            Verification verificationCode = verificationRepository.findTopByUserOrderByExpirationDateDesc(user);
            if (verificationCode != null) {
                return verificationCode.getExpirationDate();
            }
        }
        return null;
    }

}
