package com.example.demo.wordrelation;

import com.example.demo.OnlyAlphanumericOrSpaces;
import jakarta.validation.constraints.NotBlank;

public record CreateWordRelationDTO(@NotBlank @OnlyAlphanumericOrSpaces String firstWord,
                                    @NotBlank @OnlyAlphanumericOrSpaces String secondWord,
                                    @NotBlank @OnlyAlphanumericOrSpaces String type) {

}
