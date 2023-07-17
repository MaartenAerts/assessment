package com.example.demo;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/word-relation")
@AllArgsConstructor
public class WordRelationController {
    private WordRelationService wordRelationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WordRelationDTO createWordRelation(@Valid @RequestBody CreateWordRelationDTO command) {
        return WordRelationDTO.of(wordRelationService.create(new WordRelation(command.firstWord(), command.secondWord(), command.type())));
    }

    @GetMapping
    public List<WordRelationDTO> findAll(@RequestParam(value = "type", required = false) String type) {
        return wordRelationService.findAll(type).stream().map(WordRelationDTO::of).toList();
    }
}
