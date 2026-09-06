package com.pruebatecnica.todoapp.dto.response;
import com.pruebatecnica.todoapp.entity.enums.TaskStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record TaskResponse(
        UUID id,
        String title,
        String description,
        LocalDateTime creationDate,
        LocalDate dueDate,
        TaskStatus status
) {}
