package com.workable.movierama.models.dto.rating;

import com.workable.movierama.models.enums.RatingType;
import jakarta.validation.constraints.NotNull;

public record CreateRatingDto(
        @NotNull
        RatingType type,
        @NotNull
        Long movieId) {
}
