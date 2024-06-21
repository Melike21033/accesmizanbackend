package com.mizanlabs.mr.entities;

import lombok.Data;

@Data
public class SetPasswordRequest {
    private String email;
    private String password;
}
