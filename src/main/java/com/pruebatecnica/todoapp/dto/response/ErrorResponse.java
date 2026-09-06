package com.pruebatecnica.todoapp.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse( 
    LocalDateTime timestamp,
    int status,
    String error,
    String message,
    List<String> errors

) {

    public ErrorResponse(int status, String error, String message) {
        this(LocalDateTime.now(), status, error, message, List.of());
    }

    public ErrorResponse( int status, String error, String message, List<String> errors) {
        this(LocalDateTime.now(), status, error, message, errors);
    }
   
}
