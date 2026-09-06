package com.pruebatecnica.todoapp.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;

public record CreateTaskRequest(
    @NotBlank (message = "El título de la tarea es obligatorio")
    String title,
    
    String description,
    LocalDate dueDate
) {
}
   
