package com.example.demo.exception;

import java.io.File;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception ex) {
        if (ex instanceof UnauthorizedException) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Unauthorized.");
        }

        if (ex instanceof NoResourceFoundException) {
            try {
                // TODO: read this file into memory only once in prod
                File indexFile = new File("../frontend/dist/index.html");
                if (indexFile.exists()) {
                    Resource indexResource = new FileSystemResource(indexFile);
                    return ResponseEntity.ok()
                            .contentType(MediaType.TEXT_HTML)
                            .body(indexResource);
                } else {
                    return ResponseEntity
                            .status(HttpStatus.NOT_FOUND)
                            .body("Resource not found");
                }
            } catch (Exception e) {
                logError(e);
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error serving frontend");
            }
        }

        logError(ex);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred.");
    }

    private void logError(Exception ex) {
        String message;
        if (ex == null) {
            message = "An unknown error occurred";
        } else {
            message = "An unexpected error occurred: " + ex.getMessage();
        }
        System.err.println(message);
    }

}
