package com.workable.movierama.repositories;

import com.workable.movierama.models.dto.movierating.MovieRatingResultSet;
import com.workable.movierama.models.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    @Query(value = """ 
            WITH aggregated_ratings AS (
                            SELECT
                                movie_id,
                                COUNT(*) FILTER (WHERE type = 0) AS like_count,
                                COUNT(*) FILTER (WHERE type = 1) AS hate_count
                            FROM
                                movierama.rating
                            GROUP BY
                                movie_id
                        )
                        SELECT
                            m.id,
                            m.title,
                            m.description,
                            m.genre as "genre",
                            m.created_by as "createdBy",
                            m.created_date as "createdDate",
                            m.last_modified_date as "lastModifiedDate",
                            COALESCE(ar.like_count, 0) AS likes,
                            COALESCE(ar.hate_count, 0) AS hates,
                            ur.type AS "userRating",
                            ur.id as "ratingId"
                        FROM
                            movierama.movie m
                        LEFT JOIN
                            aggregated_ratings ar
                            ON m.id = ar.movie_id
                        LEFT JOIN
                            movierama.rating ur
                            ON m.id = ur.movie_id
                            AND ur.created_by = :username
                        ORDER BY
                            m.id""", nativeQuery = true)
    List<MovieRatingResultSet> getMovieRatingsByUser(@Param("username") String username);
}
