package com.example.tubesPBW.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long userID;
    private String name;
    private String username;
    private String email;
    private String password; // Ini nanti berisi hash dari database
    private String role;
}