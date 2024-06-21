package com.mizanlabs.mr.entities;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
public class Verification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    private String code;

    @NotNull
    private LocalDateTime expirationDate;
}
