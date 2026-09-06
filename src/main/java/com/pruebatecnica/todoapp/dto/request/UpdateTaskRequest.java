package com.pruebatecnica.todoapp.dto.request;

import java.time.LocalDate;

import com.pruebatecnica.todoapp.entity.enums.TaskStatus;

import jakarta.validation.constraints.Size;

public record UpdateTaskRequest(
    @Size(min = 1, message = "El título no puede estar vacío")
    String title,
    String description,
    LocalDate dueDate,
    TaskStatus status
) {
    
}
