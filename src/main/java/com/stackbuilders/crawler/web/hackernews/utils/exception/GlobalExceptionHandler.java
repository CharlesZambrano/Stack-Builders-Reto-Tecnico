package com.stackbuilders.crawler.web.hackernews.utils.exception;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidDataAccessResourceUsageException.class)
    public ResponseEntity<ErrorResponse> handleDatabaseExceptions(InvalidDataAccessResourceUsageException ex,
            WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Database Error")
                .code("DATABASE_ERROR")
                .message("Ocurrió un error al acceder a la base de datos")
                .details(List.of(ex.getMessage()))
                .path(request.getDescription(false))
                .method(request.getHeader("X-HTTP-Method-Override"))
                .exception(ErrorResponse.ExceptionDetail.builder()
                        .type(ex.getClass().getName())
                        .trace(ex.getStackTrace())
                        .build())
                .userMessage("Hubo un problema con la base de datos. Por favor, inténtelo más tarde.")
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .code("INTERNAL_SERVER_ERROR")
                .message("Error inesperado al procesar la solicitud")
                .details(List.of(ex.getMessage()))
                .path(request.getDescription(false))
                .method(request.getHeader("X-HTTP-Method-Override"))
                .exception(ErrorResponse.ExceptionDetail.builder()
                        .type(ex.getClass().getName())
                        .trace(ex.getStackTrace())
                        .build())
                .userMessage("Ocurrió un error interno. Por favor, inténtelo más tarde.")
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
