package com.unclecole.dominionfun.utils;

public enum Number {

    THOUSAND('k', 1000L),
    MILLION('m', 1000000L),
    BILLION('b', 1000000000L);

    private char character;
    private long multi;

    Number(char character, long multi) {
        this.character = character;
        this.multi = multi;
    }

    public char getCharacter() {
        return character;
    }
    public long getMulti() {
        return multi;
    }
}
