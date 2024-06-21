package com.mizanlabs.mr.entities;

import lombok.Data;

@Data
public class CreateUserRequest {
    public String role;
    private String name;
    private String email;
}
