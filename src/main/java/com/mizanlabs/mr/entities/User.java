package com.mizanlabs.mr.entities;
import lombok.Data;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    @Email
    @Column(unique = true)
    private String email;

    private String password;

    @Column(name = "is_active")
    private Boolean isActive = false;

    @Column(name = "role")
    private String role;

}

