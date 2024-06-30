package com.mizanlabs.mr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@SpringBootApplication
public class MizanBase {
    public static void main(String[] args) {
        SpringApplication.run(MizanBase.class, args);
    }   
    
}
 