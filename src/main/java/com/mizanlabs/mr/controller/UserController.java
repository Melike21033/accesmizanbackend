package com.mizanlabs.mr.controller;

import com.mizanlabs.mr.entities.CreateUserRequest;
import com.mizanlabs.mr.entities.SetPasswordRequest;
import com.mizanlabs.mr.entities.VerifyCodeRequest;
import com.mizanlabs.mr.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/users")
    public ResponseEntity<String> createUser(@RequestBody CreateUserRequest request) {
        userService.createUser(request.getName(), request.getEmail(), request.getRole());
        return ResponseEntity.ok("User created");
    }

    @PostMapping("/send-verification-code")
    public ResponseEntity<String> sendVerificationCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        userService.sendVerificationCode(email);
        return ResponseEntity.ok("Verification email sent");
    }

    @PostMapping("/verify-code")
    public ResponseEntity<String> verifyCode(@RequestBody VerifyCodeRequest request) {
        boolean isValid = userService.verifyCode(request.getEmail(), request.getCode());
        if (isValid) {
            return ResponseEntity.ok("Code verified");
        } else {
            return ResponseEntity.status(400).body("Invalid or expired code");
        }
    }

    @PostMapping("/set-password")
    public ResponseEntity<String> setPassword(@RequestBody SetPasswordRequest request) {
        userService.setPassword(request.getEmail(), request.getPassword());
        return ResponseEntity.ok("Password set successfully");
    }
}
