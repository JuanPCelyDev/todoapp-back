package com.pruebatecnica.todoapp.dto.response;

public record AuthResponse(
    String token,
    String type,
    String email,
    String username
) {
    public AuthResponse(String token, String email, String username) {
        this(token, "Bearer", email, username);
    }
    
}
