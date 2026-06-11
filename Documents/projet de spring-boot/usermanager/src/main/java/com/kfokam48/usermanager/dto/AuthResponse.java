package com.kfokam48.usermanager.dto;

public class AuthResponse {
    private String token;

    // Constructeurs
    public AuthResponse() {}

    public AuthResponse(String token) {
        this.token = token;
    }

    // Getter et Setter
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}