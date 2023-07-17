package com.example.demo;

import jakarta.validation.constraints.NotBlank;

public record CreateWordRelationDTO(@NotBlank @OnlyAlphanumericOrSpaces String firstWord,
                                    @NotBlank @OnlyAlphanumericOrSpaces String secondWord,
                                    @NotBlank @OnlyAlphanumericOrSpaces String type) {

}
