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
import org.apache.commons.lang3.builder.EqualsBuilder;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        WordRelation that = (WordRelation) o;

        return new EqualsBuilder().append(getId(), that.getId()).isEquals();
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
