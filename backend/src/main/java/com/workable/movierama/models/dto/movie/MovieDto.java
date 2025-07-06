package com.workable.movierama.models.dto.movie;

import com.workable.movierama.models.entities.Movie;
import com.workable.movierama.models.enums.MovieGenre;

import java.time.Instant;

public record MovieDto(
        Long id,
        String title,
        String description,
        MovieGenre genre,
        String createdBy,
        Instant createdDate,
        Instant lastModifiedDate) {

    public MovieDto(Movie movie) {
        this(movie.getId(), movie.getTitle(), movie.getDescription(), movie.getGenre(), movie.getCreatedBy(), movie.getCreatedDate(), movie.getLastModifiedDate());
    }
}
