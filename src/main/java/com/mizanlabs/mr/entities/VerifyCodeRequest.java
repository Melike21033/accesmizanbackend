package com.mizanlabs.mr.entities;

import lombok.Data;

@Data
public class VerifyCodeRequest {
    private String email;
    private String code;
}
