package com.example.demo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "word_relation")
@Entity
public class WordRelation {
    @Id
    @GeneratedValue
    private Long id;

    private String firstWord;
    private String secondWord;
    private String type;

    public WordRelation(@NotBlank String firstWord, @NotBlank String secondWord, @NotBlank String type) {
        this.firstWord = firstWord;
        this.secondWord = secondWord;
        this.type = type;
    }
}
