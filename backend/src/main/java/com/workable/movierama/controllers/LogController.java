package com.workable.movierama.controllers;

import com.workable.movierama.models.dto.log.NgxLoggerDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("log")
public class LogController {

    private final Logger logger = LoggerFactory.getLogger(LogController.class);

    @PostMapping
    public ResponseEntity<Void> log(@RequestBody NgxLoggerDto trace) {
        logger.info("Movierama-fe: {}", trace.message());
        return ResponseEntity.noContent().build();
    }
}
