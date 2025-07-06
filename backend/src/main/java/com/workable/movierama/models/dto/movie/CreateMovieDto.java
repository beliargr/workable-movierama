package com.workable.movierama.models.dto.movie;

import com.workable.movierama.models.enums.MovieGenre;

public record CreateMovieDto(String title, String description, MovieGenre genre) {}
