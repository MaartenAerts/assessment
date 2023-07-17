package com.example.demo.wordrelation;

import com.example.demo.wordrelation.path.PathNodeDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
    public List<WordRelationDTO> findAll(@RequestParam(value = "type", required = false) String type, @RequestParam(value = "inverse", required = false) boolean inverse) {
        List<WordRelation> relations = wordRelationService.findAll(type);
        List<WordRelationDTO> relationDtos = relations.stream().map(WordRelationDTO::of).collect(Collectors.toList());
        if (inverse) {
            relationDtos.addAll(relations.stream().map(WordRelationDTO::inverse).toList());
        }
        return relationDtos;
    }

    @GetMapping("/path/{source}/{target}")
    public List<PathNodeDTO> findPath(@PathVariable("source") String source, @PathVariable("target") String target) {
        return wordRelationService.findPath(source, target).stream().map(PathNodeDTO::new).toList();
    }

}
