package com.example.demo;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class WordRelationService {
    private WordRelationRepository repository;

    public WordRelation create(WordRelation wordRelation) {
        return repository.save(wordRelation);
    }

    @Transactional(readOnly = true)
    public List<WordRelation> findAll(String type) {
        if (StringUtils.isBlank(type)) {
            return repository.findAll();
        }
        return repository.findByType(type);
    }
}
