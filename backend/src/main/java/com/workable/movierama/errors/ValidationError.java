package com.workable.movierama.errors;

public class ValidationError extends RuntimeException {

    private final String errorCode;
    public ValidationError(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
