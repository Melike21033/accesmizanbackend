package com.mizanlabs.mr.service;

import com.mizanlabs.mr.entities.User;
import com.mizanlabs.mr.entities.Verification;
import com.mizanlabs.mr.repository.UserRepository;
import com.mizanlabs.mr.repository.VerificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationRepository verificationCodeRepository;

    @Autowired
    private EmailService emailService;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void createUser(String name, String email,String role) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setRole(role);
        userRepository.save(user);
    }

    public void sendVerificationCode(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            String code = UUID.randomUUID().toString();
            Verification verificationCode = new Verification();
            verificationCode.setUser(user);
            verificationCode.setCode(code);
            verificationCode.setExpirationDate(LocalDateTime.now().plusMinutes(15));
            verificationCodeRepository.save(verificationCode);

            emailService.sendVerificationEmail(email, code);
        }
    }

    public boolean verifyCode(String email, String code) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return false;
        }

        Verification verificationCode = verificationCodeRepository.findByUserAndCode(user, code);
        if (verificationCode == null || verificationCode.getExpirationDate().isBefore(LocalDateTime.now())) {
            return false;
        }

        return true;
    }

    public void setPassword(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.setPassword(passwordEncoder.encode(password));
            user.setIsActive(true);
            userRepository.save(user);
        }
    }
}
