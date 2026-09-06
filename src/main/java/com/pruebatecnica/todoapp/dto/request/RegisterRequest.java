package com.pruebatecnica.todoapp.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

    @NotBlank(message = "El nombre de usuario es obligatorio")
    String username,
    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email (message = "El correo electrónico no es válido")
    String email,
    @NotBlank(message = "La contraseña es obligatoria")
    @Size (min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    String password
) {
}
