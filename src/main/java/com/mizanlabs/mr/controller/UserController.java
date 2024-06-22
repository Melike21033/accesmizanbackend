package com.mizanlabs.mr.controller;

import com.mizanlabs.mr.entities.CreateUserRequest;
import com.mizanlabs.mr.entities.SetPasswordRequest;
import com.mizanlabs.mr.entities.VerifyCodeRequest;
import com.mizanlabs.mr.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/users")
    public ResponseEntity<Map<String, String>> createUser(@RequestBody CreateUserRequest request) {
        userService.createUser(request.getName(), request.getEmail(), request.getRole());
        Map<String, String> response = new HashMap<>();
        response.put("message", "User created");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/send-verification-code")
    public ResponseEntity<Map<String, String>> sendVerificationCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        userService.sendVerificationCode(email);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Verification email sent");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify-code")
    public ResponseEntity<Map<String, String>> verifyCode(@RequestBody VerifyCodeRequest request) {
        boolean isValid = userService.verifyCode(request.getEmail(), request.getCode());
        Map<String, String> response = new HashMap<>();
        if (isValid) {
            response.put("message", "Code verified");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Invalid or expired code");
            return ResponseEntity.status(400).body(response);
        }
    }

    @PostMapping("/set-password")
    public ResponseEntity<Map<String, String>> setPassword(@RequestBody SetPasswordRequest request) {
        userService.setPassword(request.getEmail(), request.getPassword());
        Map<String, String> response = new HashMap<>();
        response.put("message", "Password set successfully");
        return ResponseEntity.ok(response);
    }
}
