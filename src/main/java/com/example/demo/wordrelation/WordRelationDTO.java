package com.example.demo.wordrelation;

public record WordRelationDTO(String firstWord, String secondWord, String type, boolean inverse) {
    public static WordRelationDTO of(WordRelation wordRelation) {
        return new WordRelationDTO(wordRelation.getFirstWord(), wordRelation.getSecondWord(), wordRelation.getType(), false);
    }
    public static WordRelationDTO inverse(WordRelation wordRelation) {
        return new WordRelationDTO(wordRelation.getSecondWord(), wordRelation.getFirstWord(), wordRelation.getType(), true);
    }
}
