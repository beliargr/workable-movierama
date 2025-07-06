package com.workable.movierama.models.dto.movie;

import com.workable.movierama.models.enums.MovieGenre;

public record UpdateMovieDto(
        String title,
        String description,
        MovieGenre genre) {}
