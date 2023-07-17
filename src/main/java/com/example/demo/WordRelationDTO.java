package com.example.demo;

public record WordRelationDTO(String firstWord, String secondWord, String type) {
    public static WordRelationDTO of(WordRelation wordRelation) {
        return new WordRelationDTO(wordRelation.getFirstWord(), wordRelation.getSecondWord(), wordRelation.getType());
    }
}
