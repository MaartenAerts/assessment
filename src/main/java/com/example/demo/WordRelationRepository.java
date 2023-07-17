package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WordRelationRepository extends JpaRepository<WordRelation, Long> {
    List<WordRelation> findByType(String type);

    boolean existsByFirstWordAndSecondWord(String secondWord, String firstWord);
}
