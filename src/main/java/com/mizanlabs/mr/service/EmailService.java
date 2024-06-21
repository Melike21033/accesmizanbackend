package com.mizanlabs.mr.service;

public interface EmailService {
    void sendVerificationEmail(String to, String code);
}

