package com.workable.movierama.models.dto.log;

public record NgxLoggerDto(
        Long columnNumber,
        String fileName,
        String level,
        Long lineNumber,
        String message,
        String timestamp
) {
}
