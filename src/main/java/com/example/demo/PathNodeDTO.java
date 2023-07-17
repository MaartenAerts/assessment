package com.example.demo;

public record PathNodeDTO(String start, String type, String end) {
    public PathNodeDTO(PathNode pathNode) {
        this(pathNode.start(),pathNode.wordRelation().getType(), pathNode.end());
    }
}
