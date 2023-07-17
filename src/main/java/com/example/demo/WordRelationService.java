package com.example.demo;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WordRelationService {
    private WordRelationRepository repository;

    public WordRelation create(WordRelation wordRelation) {
        return repository.save(wordRelation);
    }
}
