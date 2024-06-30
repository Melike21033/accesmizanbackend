package com.mizanlabs.mr.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendVerificationEmail(String to, String code) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject("Code de Vérification");

            String htmlMsg = "<html>" +
                    "<body style='font-family: Arial, sans-serif;'>" +
                    "<div style='max-width: 600px; margin: auto; padding: 20px; border: 1px solid #eee; border-radius: 10px; background: #f9f9f9;'>" +
                    "<h2 style='color: #333;'>Code de Vérification</h2>" +
                    "<p style='font-size: 1.1em;'>Bonjour,</p>" +
                    "<p style='font-size: 1.1em;'>Nous sommes heureux de vous accueillir dans notre équipe.</p>" +
                    "<p style='font-size: 1.1em;'>Votre code de vérification est :</p>" +
                    "<div style='text-align: center; margin: 20px 0;'>" +
                    "<span style='display: inline-block; padding: 10px 20px; font-size: 1.5em; color: white; background: #007bff; border-radius: 5px;'>" + code + "</span>" +
                    "</div>" +
                    "<p style='font-size: 1.1em;'>Ce code expirera dans 15 minutes.</p>" +
                    "<p style='font-size: 1.1em;'>Si vous n'avez pas demandé ce code, veuillez ignorer cet email.</p>" +
                    "<p style='font-size: 1.1em;'>Cordialement,</p>" +
                    "<p style='font-size: 1.1em;'>L'équipe Mizan Labs</p>" +
                    "</div>" +
                    "</body>" +
                    "</html>";

            helper.setText(htmlMsg, true); // true indique que le contenu est en HTML

            mailSender.send(message);
        } catch (jakarta.mail.MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
