package com.example.demo;

public record ErrorDTO(String message, String field) {
    public ErrorDTO(String message) {
        this(message, null);
    }
}
