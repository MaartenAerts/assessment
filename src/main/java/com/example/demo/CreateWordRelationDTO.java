package com.example.demo;

import jakarta.validation.constraints.NotBlank;

public record CreateWordRelationDTO(@NotBlank String firstWord, @NotBlank String secondWord, @NotBlank String type) {

}
