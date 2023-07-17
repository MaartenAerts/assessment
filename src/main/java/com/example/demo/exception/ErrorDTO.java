package com.example.demo.exception;

public record ErrorDTO(String message, String field) {
    public ErrorDTO(String message) {
        this(message, null);
    }
}
