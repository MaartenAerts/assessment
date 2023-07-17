package com.example.demo;

public record PathNode(WordRelation wordRelation, boolean inverse) {
    public String start() {
        return inverse ? wordRelation.getSecondWord() : wordRelation.getFirstWord();
    }

    public String end() {
        return inverse ? wordRelation.getFirstWord() : wordRelation.getSecondWord();
    }
}