package be.maartenaerts.assessment.wordrelation;

import be.maartenaerts.assessment.exception.ConflictException;
import be.maartenaerts.assessment.exception.NotFoundException;
import be.maartenaerts.assessment.wordrelation.path.PathNode;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayDeque;
import java.util.List;
import java.util.stream.Stream;

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

    @Transactional(readOnly = true)
    public List<PathNode> findPath(String source, String target) {
        List<List<PathNode>> seedPaths = repository.findByFirstWordOrSecondWord(source, source).stream()
                .map(r -> List.of(new PathNode(r, !r.getFirstWord().equals(source))))
                .toList();
        ArrayDeque<List<PathNode>> queue = new ArrayDeque<>(seedPaths);

        while (!queue.isEmpty()) {
            List<PathNode> path = queue.poll();
            String current = path.get(path.size() - 1).end();
            if (current.equals(target)) {
                return path;
            } else {
                repository.findByFirstWordOrSecondWord(current, path.stream().map(PathNode::wordRelation).toList())
                        .stream().map(r -> Stream.concat(path.stream(), Stream.of(new PathNode(r, !r.getFirstWord().equals(current)))).toList())
                        .forEach(queue::add);
            }
        }
        throw new NotFoundException("No path found");
    }
}
