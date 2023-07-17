package be.maartenaerts.assessment.wordrelation;

import be.maartenaerts.assessment.OnlyAlphanumericOrSpaces;
import jakarta.validation.constraints.NotBlank;

public record CreateWordRelationDTO(@NotBlank @OnlyAlphanumericOrSpaces String firstWord,
                                    @NotBlank @OnlyAlphanumericOrSpaces String secondWord,
                                    @NotBlank @OnlyAlphanumericOrSpaces String type) {

}
