package com.workable.movierama.models.dto.rating;

import com.workable.movierama.models.entities.Rating;
import com.workable.movierama.models.enums.RatingType;

import java.time.Instant;

public record RatingDto(
        Long id,
        Long movieId,
        RatingType type,
        String createdBy,
        Instant createdDate,
        String lastModifiedBy) {

    public RatingDto(Rating rating) {
        this(rating.getId(), rating.getMovie().getId() ,rating.getType(), rating.getCreatedBy(), rating.getCreatedDate(), rating.getLastModifiedBy());
    }
}
