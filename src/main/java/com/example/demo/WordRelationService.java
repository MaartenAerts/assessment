package com.example.demo;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Collections.*;

@Service
@AllArgsConstructor
@Transactional
public class WordRelationService {
    private WordRelationRepository repository;

    public WordRelation create(WordRelation wordRelation) {
        if (repository.existsByFirstWordAndSecondWord(wordRelation.getSecondWord(), wordRelation.getFirstWord())) {
//            Can't be done with constraint in in-memory database
            throw new ConflictException("Inverse relation already exists");
        }
        return repository.save(wordRelation);
    }

    @Transactional(readOnly = true)
    public List<WordRelation> findAll(String type) {
        if (StringUtils.isBlank(type)) {
            return repository.findAll();
        }
        return repository.findByType(type);
    }

    public List<PathNode> findPath(String source, String target) {
        List<WordRelation> relations = repository.findByFirstWordOrSecondWord(source, source);
        for (WordRelation wordRelation :
                relations) {
            List<PathNode> path = singletonList(new PathNode(wordRelation, !wordRelation.getFirstWord().equals(source)));
            if (path.get(0).end().equals(target)) {
                return path;
            } else {
                List<PathNode> resultPath = findPath(target, path);
                if (resultPath.get(resultPath.size() - 1).end().equals(target)) {
                    return resultPath;
                }
            }
        }
        throw new NotFoundException("No path found");
    }

    private List<PathNode> findPath(String target, List<PathNode> path) {
        String previousEnd = path.get(path.size() - 1).end();
        List<WordRelation> relations = repository.findByFirstWordOrSecondWord(previousEnd, path.stream().map(PathNode::wordRelation).toList());
        for (WordRelation relation :
                relations) {
            PathNode newNode = new PathNode(relation, !relation.getFirstWord().equals(previousEnd));
            List<PathNode> extendedPath = Stream.concat(path.stream(), Stream.of(newNode)).toList();
            if (newNode.end().equals(target)) {
                return extendedPath;
            } else {
                return findPath(target, extendedPath);
            }
        }
        return emptyList();
    }
}
