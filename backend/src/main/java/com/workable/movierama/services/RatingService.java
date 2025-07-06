package com.workable.movierama.services;

import com.workable.movierama.errors.ValidationError;
import com.workable.movierama.models.dto.rating.CreateRatingDto;
import com.workable.movierama.models.dto.rating.RatingDto;
import com.workable.movierama.models.dto.rating.UpdateRatingDto;
import com.workable.movierama.models.entities.Movie;
import com.workable.movierama.models.entities.Rating;
import com.workable.movierama.repositories.MovieRepository;
import com.workable.movierama.repositories.RatingRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RatingService {

    private final RatingRepository ratingRepository;
    private final MovieRepository movieRepository;
    private final SecurityService securityService;

    public RatingService(RatingRepository ratingRepository, MovieRepository movieRepository, SecurityService securityService) {
        this.ratingRepository = ratingRepository;
        this.movieRepository = movieRepository;
        this.securityService = securityService;
    }

    /**
     * Creates a new rating for a movie.
     * <p>
     * Evicts all entries from the "ratings" and "movierating" caches to ensure consistency.
     *
     * @param dto the data transfer object containing the rating details
     * @return the created {@link Rating} entity
     * @throws EntityNotFoundException if the movie does not exist
     */
    @Caching(evict = {
            @CacheEvict(value = "ratings", allEntries = true),
            @CacheEvict(cacheNames = "movierating", allEntries = true)
    })
    public Rating create(CreateRatingDto dto) {
        Movie movie = movieRepository.findById(dto.movieId())
                .orElseThrow(() -> new EntityNotFoundException("Movie not found"));
        Rating rating = new Rating();
        rating.setType(dto.type());
        rating.setMovie(movie);
        return ratingRepository.save(rating);
    }

    /**
     * Updates an existing rating.
     * <p>
     * Evicts all entries from the "ratings" and "movierating" caches to ensure consistency.
     *
     * @param id  the ID of the rating to update
     * @param dto the data transfer object containing the updated rating details
     * @return the updated {@link Rating} entity
     * @throws EntityNotFoundException if the rating does not exist
     */
    @Caching(evict = {
            @CacheEvict(value = "ratings", allEntries = true),
            @CacheEvict(cacheNames = "movierating", allEntries = true)
    })
    public Rating update(Long id, UpdateRatingDto dto) {
        Rating rating = ratingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rating not found"));
        rating.setType(dto.type());
        return ratingRepository.save(rating);
    }

    /**
     * Retrieves all ratings.
     *
     * @return a list of all {@link Rating} entities
     */
    public List<Rating> findAll() {
        return ratingRepository.findAll();
    }

    /**
     * Retrieves all ratings as DTOs.
     * <p>
     * Results are cached under the "ratings" cache.
     *
     * @return a list of {@link RatingDto} objects
     */
    @Cacheable(value = "ratings")
    public List<RatingDto> getRatings() {
        return ratingRepository.findAll().stream().map(RatingDto::new).toList();
    }

    /**
     * Finds a rating by its ID.
     *
     * @param id the ID of the rating
     * @return an {@link Optional} containing the {@link Rating} entity if found
     */
    public Optional<Rating> findById(Long id) {
        return ratingRepository.findById(id);
    }

    /**
     * Retrieves a rating by its ID as a DTO.
     * <p>
     * Result is cached under the "ratings" cache with the rating ID as the key.
     *
     * @param id the ID of the rating
     * @return an {@link Optional} containing the {@link RatingDto} if found
     */
    @Cacheable(value = "ratings", key = "#id")
    public Optional<RatingDto> getRating(Long id) {
        return ratingRepository.findById(id).map(RatingDto::new);
    }

    /**
     * Deletes a rating by its ID.
     * <p>
     * Evicts all entries from the "ratings" and "movierating" caches to ensure consistency.
     *
     * @param id the ID of the rating to delete
     */
    @Caching(evict = {
            @CacheEvict(value = "ratings", allEntries = true),
            @CacheEvict(cacheNames = "movierating", allEntries = true)
    })
    public void delete(Long id) {
        ratingRepository.deleteById(id);
    }

    /**
     * Rates a movie by creating or updating a rating for the movie.
     * <p>
     * If the user has already rated the movie, the existing rating is updated.
     * If the user is the creator of the movie, an error is thrown.
     * <p>
     * Evicts all entries from the "ratings" and "movierating" caches to ensure consistency.
     *
     * @param id  the ID of the movie to rate
     * @param dto the data transfer object containing the rating details
     * @return the created or updated {@link Rating} entity
     * @throws ValidationError if the user attempts to rate their own movie
     * @throws EntityNotFoundException if the movie does not exist
     */
    @Caching(evict = {
            @CacheEvict(value = "ratings", allEntries = true),
            @CacheEvict(cacheNames = "movierating", allEntries = true)
    })
    public Rating rateMovie(Long id, UpdateRatingDto dto) {
        List<Rating> ratings = ratingRepository.findAllByCreatedByAndMovie_Id(securityService.getUsername(), id);
        Optional<Movie> movie = this.movieRepository.findById(id);
        Optional<Rating> maybeRating = Optional.ofNullable(ratings)
                .orElse(List.of())
                .stream()
                .findFirst();

        if (movie.isPresent()) {
            if (movie.get().getCreatedBy().equals(securityService.getUsername()))
                throw new ValidationError("USER_RATING_ERROR", "User cannot rate a movie he has uploaded");
        }

        Rating rating;
        if (maybeRating.isPresent()) {
            rating = maybeRating.get();
            rating.setType(dto.type());
        } else {
            rating = new Rating();
            rating.setType(dto.type());
            rating.setMovie(movie.get());
        }
        return this.ratingRepository.save(rating);
    }
}
