package com.workable.movierama.models.dto.movierating;

import com.workable.movierama.models.enums.RatingType;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.SqlResultSetMapping;

import java.time.Instant;

@SqlResultSetMapping(
        name = "MovieRatingDtoMapping",
        classes = @ConstructorResult(
                targetClass = MovieRatingResultSet.class,
                columns = {
                        @ColumnResult(name = "id", type = Long.class),
                        @ColumnResult(name = "title", type = String.class),
                        @ColumnResult(name = "description", type = String.class),
                        @ColumnResult(name = "genre", type = Short.class),
                        @ColumnResult(name = "createdBy", type = String.class),
                        @ColumnResult(name = "createdDate", type = Instant.class),
                        @ColumnResult(name = "lastModifiedDate", type = Instant.class),
                        @ColumnResult(name = "likes", type = Long.class),
                        @ColumnResult(name = "hates", type = Long.class),
                        @ColumnResult(name = "userRating", type = Short.class),
                        @ColumnResult(name = "ratingId", type = Long.class)
                }
        )
)
public record MovieRatingResultSet(
        Long id,
        String title,
        String description,
        Short genre,
        String createdBy,
        Instant createdDate,
        Instant lastModifiedDate,
        Long likes,
        Long hates,
        Short userRating,
        Long ratingId) {

}
