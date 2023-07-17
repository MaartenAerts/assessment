package be.maartenaerts.assessment.wordrelation;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "word_relation")
@Entity
@ToString
public class WordRelation {
    @Id
    @GeneratedValue
    private Long id;

    private String firstWord;
    private String secondWord;
    private String type;

    public WordRelation(@NotBlank String firstWord, @NotBlank String secondWord, @NotBlank String type) {
        this.firstWord = firstWord.toLowerCase().trim();
        this.secondWord = secondWord.toLowerCase().trim();
        this.type = type.toLowerCase().trim();
    }
}
