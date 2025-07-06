DROP VIEW IF EXISTS v_movie_ratings;

create view v_movie_ratings as
with aggregated_ratings as (
SELECT
  movie_id,
  COUNT(*) FILTER (WHERE type = 0) AS like_count,
  COUNT(*) FILTER (WHERE type = 1) AS hate_count
    FROM
      rating
    GROUP BY
      movie_id
    ORDER BY
      movie_id
 ) select m.*, ar.like_count, ar.hate_count
 from movie m
 left join aggregated_ratings ar
 on m.id = ar.movie_id
