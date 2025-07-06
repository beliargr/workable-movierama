package com.workable.movierama.models.dto.movierating;

import com.workable.movierama.models.enums.MovieGenre;
import com.workable.movierama.models.enums.RatingType;

import java.time.Instant;

public record MovieRatingDto(
        Long id,
        String title,
        String description,
        MovieGenre genre,
        String createdBy,
        Instant createdDate,
        Instant lastModifiedDate,
        Long likes,
        Long hates,
        RatingType userRating,
        Long ratingId) {

    public MovieRatingDto(MovieRatingResultSet that) {
        this(that.id(),
                that.title(),
                that.description(),
                that.genre() != null ? MovieGenre.fromCode(that.genre())  : null,
                that.createdBy(),
                that.createdDate(),
                that.lastModifiedDate(),
                that.likes(),
                that.hates(),
                that.userRating() != null ? RatingType.fromCode(that.userRating()) : null,
                that.ratingId()
        );
    }
}
