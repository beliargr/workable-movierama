package com.workable.movierama.models.enums;

public enum MovieGenre {
    ACTION((short) 0),
    COMEDY((short) 1),
    DRAMA((short) 2),
    HORROR((short) 3),
    ROMANCE((short) 4),
    SCIENCE((short) 5);

    private final short value;

    MovieGenre(short value1) {
        this.value = value1;
    }

    public static MovieGenre fromCode(short code) {
        for (MovieGenre n : values()) {
            if (n.value == code) return n;
        }
        throw new IllegalArgumentException("Unknown code: " + code);
    }

}
