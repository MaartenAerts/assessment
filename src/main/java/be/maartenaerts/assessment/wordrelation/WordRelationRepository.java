package be.maartenaerts.assessment.wordrelation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WordRelationRepository extends JpaRepository<WordRelation, Long> {
    List<WordRelation> findByType(String type);

    boolean existsByFirstWordAndSecondWord(String firstWord, String secondWord);

    @Query("select wr from WordRelation wr where (wr.firstWord=?1 or wr.secondWord=?1) and wr not in ?2")
    List<WordRelation> findByFirstWordOrSecondWord(String firstWord, List<WordRelation> exclude);

    List<WordRelation> findByFirstWordOrSecondWord(String firstWord, String secondWord);
}
