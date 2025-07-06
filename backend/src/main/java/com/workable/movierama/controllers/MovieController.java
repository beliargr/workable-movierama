package com.workable.movierama.controllers;

import com.workable.movierama.models.dto.movie.CreateMovieDto;
import com.workable.movierama.models.dto.movie.MovieDto;
import com.workable.movierama.models.dto.movie.UpdateMovieDto;
import com.workable.movierama.models.dto.movierating.MovieRatingDto;
import com.workable.movierama.services.MovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController()
@RequestMapping("movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    /**
     * Get a movie by its ID.
     *
     * @param id the ID of the movie
     * @return ResponseEntity containing the MovieDto if found, or 404 Not Found if not
     */
    @GetMapping("{id}")
    public ResponseEntity<MovieDto> getById(@PathVariable Long id) {
        return movieService.getMovie(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get all movies.
     *
     * @return ResponseEntity containing a list of MovieDto
     */
    @GetMapping()
    public ResponseEntity<List<MovieDto>> getAll() {
        return ResponseEntity.ok(
                movieService.getMovies()
        );
    }

    /**
     * Get all movies with their ratings.
     *
     * @return ResponseEntity containing a list of MovieRatingDto
     */
    @GetMapping("/ratings")
    public ResponseEntity<List<MovieRatingDto>> getMoviesWithRatings() {
        return ResponseEntity.ok(
                movieService.getMoviesWithRatings()
        );
    }

    /**
     * Create a new movie.
     *
     * @param dto the CreateMovieDto containing movie details
     * @return ResponseEntity containing the created MovieDto
     */
    @PostMapping
    public ResponseEntity<MovieDto> create(@RequestBody CreateMovieDto dto) {
        return ResponseEntity.ok(new MovieDto(movieService.create(dto)));
    }

    /**
     * Update an existing movie.
     *
     * @param id  the ID of the movie to update
     * @param dto the UpdateMovieDto containing updated movie details
     * @return ResponseEntity containing the updated MovieDto
     */
    @PatchMapping({"{id}"})
    public ResponseEntity<MovieDto> update(@PathVariable Long id, @RequestBody UpdateMovieDto dto) {
        return ResponseEntity.ok(new MovieDto(movieService.update(id, dto)));
    }

    /**
     * Delete a movie by its ID.
     *
     * @param id the ID of the movie to delete
     * @return ResponseEntity with no content if successful, or 404 Not Found if the movie does not exist
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        movieService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Bulk delete movies by their IDs.
     *
     * @param body a map containing a list of IDs to delete
     * @return ResponseEntity with no content if successful
     */
    @DeleteMapping("bulk")
    public ResponseEntity<Void> delete(@RequestBody HashMap<String, Object> body) {
        movieService.bulkDelete((List<Long>) body.get("ids"));
        return ResponseEntity.noContent().build();
    }

}
