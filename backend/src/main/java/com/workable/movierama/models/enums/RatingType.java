package com.workable.movierama.models.enums;

public enum RatingType {
    LIKE((short) 0),
    HATE((short) 1);

    private final short value;

    RatingType(short value) {
        this.value = value;
    }

    public static RatingType fromCode(short code) {
        for (RatingType n : values()) {
            if (n.value == code) return n;
        }
        throw new IllegalArgumentException("Unknown code: " + code);
    }
}
