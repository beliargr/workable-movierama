package com.workable.movierama.controllers;

import com.workable.movierama.models.dto.rating.CreateRatingDto;
import com.workable.movierama.models.dto.rating.RatingDto;
import com.workable.movierama.models.dto.rating.UpdateRatingDto;
import com.workable.movierama.services.RatingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("ratings")
public class RatingController {

    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    /**
     * Handles requests for ratings.
     *
     * @param id the ID of the rating
     * @return ResponseEntity containing the rating or a not found status
     */
    @GetMapping("{id}")
    public ResponseEntity<RatingDto> getById(@PathVariable Long id) {
        return ratingService.getRating(id)
                .map(ResponseEntity::ok)
                .orElse((ResponseEntity.notFound().build()));
    }

    /**
     * Handles requests to get all ratings.
     *
     * @return ResponseEntity containing a list of all ratings
     */
    @GetMapping
    public ResponseEntity<List<RatingDto>> getAll() {
        return ResponseEntity.ok(
                ratingService.getRatings()
        );
    }

    /**
     * Handles requests to create a new rating.
     *
     * @param dto the data transfer object containing rating details
     * @return ResponseEntity containing the created rating
     */
    @PostMapping
    public ResponseEntity<RatingDto> create(@RequestBody CreateRatingDto dto) {
        return ResponseEntity.ok(new RatingDto(ratingService.create(dto)));
    }

    /**
     * Handles requests to delete a rating by ID.
     *
     * @param id the ID of the rating to delete
     * @return ResponseEntity with no content if successful
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ratingService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Handles requests to update an existing rating.
     *
     * @param id the ID of the rating to update
     * @param dto the data transfer object containing updated rating details
     * @return ResponseEntity containing the updated rating
     */
    @PatchMapping("{id}")
    public ResponseEntity<RatingDto> update(@PathVariable Long id, @RequestBody UpdateRatingDto dto) {
        return ResponseEntity.ok(new RatingDto(ratingService.update(id, dto)));
    }

    /**
     * Handles requests to rate a movie by ID.
     *
     * @param id the ID of the movie to rate
     * @param dto the data transfer object containing rating details
     * @return ResponseEntity containing the rating for the movie
     */
    @PostMapping("/rate-movie/{id}")
    public ResponseEntity<RatingDto> rateMovie(@PathVariable Long id, @RequestBody UpdateRatingDto dto) {
        return ResponseEntity.ok(new RatingDto(ratingService.rateMovie(id, dto)));
    }

}
