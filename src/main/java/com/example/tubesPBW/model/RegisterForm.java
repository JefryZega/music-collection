package com.example.tubesPBW.model;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
@NotBlank
public class RegisterForm {
    private String name;
    private String username;
    private String email;
    private String password;
    private String confirmPassword;
}