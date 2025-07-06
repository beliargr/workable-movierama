package com.workable.movierama.errors;

import org.springframework.http.HttpStatus;

import java.time.Instant;

public record ApiError(
        String type,
        Instant timestamp,
        HttpStatus status,
        String errorCode) {

    public ApiError(HttpStatus status, String errorCode) {
        this("API-ERROR", Instant.now(), status, errorCode);
    }
}
