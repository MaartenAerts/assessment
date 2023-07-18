package be.maartenaerts.assessment.wordrelation;

import be.maartenaerts.assessment.OnlyAlphabeticOrSpaces;
import jakarta.validation.constraints.NotBlank;

public record CreateWordRelationDTO(@NotBlank @OnlyAlphabeticOrSpaces String firstWord,
                                    @NotBlank @OnlyAlphabeticOrSpaces String secondWord,
                                    @NotBlank @OnlyAlphabeticOrSpaces String type) {

}
