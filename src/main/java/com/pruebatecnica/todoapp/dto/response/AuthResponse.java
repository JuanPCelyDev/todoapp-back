package com.pruebatecnica.todoapp.dto.response;

public record AuthResponse(
    String token,
    String type,
    String username,
    String email
) {
    public AuthResponse(String token, String email, String name) {
        this(token, "Bearer", email, name);
    }
    
}
