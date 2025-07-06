package com.workable.movierama.services;

import com.workable.movierama.models.dto.movie.CreateMovieDto;
import com.workable.movierama.models.dto.movie.MovieDto;
import com.workable.movierama.models.dto.movie.UpdateMovieDto;
import com.workable.movierama.models.dto.movierating.MovieRatingDto;
import com.workable.movierama.models.dto.movierating.MovieRatingResultSet;
import com.workable.movierama.models.entities.Movie;
import com.workable.movierama.models.enums.RatingType;
import com.workable.movierama.repositories.MovieRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final SecurityService securityService;

    public MovieService(MovieRepository movieRepository, SecurityService securityService) {
        this.movieRepository = movieRepository;
        this.securityService = securityService;
    }

    /**
     * Create a new movie.
     *
     * @param dto the data transfer object containing movie details
     * @return the created Movie entity
     */
    @Caching(evict = {
            @CacheEvict(value = "movies", allEntries = true),
            @CacheEvict(cacheNames = "movierating", allEntries = true)
    })
    public Movie create(CreateMovieDto dto) {
        Movie movie = new Movie();
        movie.setTitle(dto.title());
        movie.setDescription(dto.description());
        movie.setGenre(dto.genre());
        return movieRepository.save(movie);
    }

    /**
     * Find a movie by its ID.
     *
     * @param id the ID of the movie
     * @return an Optional containing the Movie entity if found, or empty if not found
     */
    public Optional<Movie> findById(Long id) {
        return movieRepository.findById(id);
    }

    /**
     * Get a movie by its ID.
     *
     * @param id the ID of the movie
     * @return an Optional containing the MovieDto if found, or empty if not found
     */
    @Cacheable(value = "movies", key = "#id")
    public Optional<MovieDto> getMovie(Long id) {
        return movieRepository.findById(id).map(MovieDto::new);
    }

    /**
     * Find all movies.
     *
     * @return a list of all Movie entities
     */
    public List<Movie> findAll() {
        return movieRepository.findAll();
    }

    /**
     * Get all movies.
     *
     * @return a list of MovieDto representing all movies
     */
    @Cacheable(value = "movies")
    public List<MovieDto> getMovies() {
        return movieRepository.findAll().stream().map(MovieDto::new).toList();
    }

    /**
     * Get movies with ratings for the current user.
     *
     * @return a list of MovieRatingDto representing movies with ratings
     */
    @Cacheable(value = "movierating")
    public List<MovieRatingDto> getMoviesWithRatings() {
        return movieRepository.getMovieRatingsByUser(securityService.getUsername()).stream().map(MovieRatingDto::new).toList();
    }

    /**
     * Updates an existing movie with new details provided in the {@link UpdateMovieDto}.
     * <p>
     * This method evicts all entries from the "movies" and "movierating" caches to ensure
     * that stale data is not served after the update.
     *
     * @param id  the ID of the movie to update
     * @param dto the data transfer object containing updated movie details
     * @return the updated {@link Movie} entity
     * @throws EntityNotFoundException if no movie with the given ID exists
     */
    @Caching(evict = {
            @CacheEvict(value = "movies", allEntries = true),
            @CacheEvict(cacheNames = "movierating", allEntries = true)
    })
    public Movie update(Long id, UpdateMovieDto dto) {
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(""));
        movie.setTitle(dto.title());
        movie.setDescription(dto.description());
        movie.setGenre(dto.genre());
        return movieRepository.save(movie);
    }

    /**
     * Deletes a movie by its ID.
     * <p>
     * This method evicts all entries from the "movies" and "movierating" caches to ensure
     * that deleted data is not served from the cache.
     *
     * @param id the ID of the movie to delete
     * @throws EntityNotFoundException if no movie with the given ID exists
     */
    @Caching(evict = {
            @CacheEvict(value = "movies", allEntries = true),
            @CacheEvict(cacheNames = "movierating", allEntries = true)
    })
    public void delete(Long id) {
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(""));
        movieRepository.delete(movie);
    }

    /**
     * Deletes multiple movies by their IDs.
     * <p>
     * This method evicts all entries from the "movies" and "movierating" caches to ensure
     * that deleted data is not served from the cache.
     *
     * @param ids a list of movie IDs to delete
     */
    @Caching(evict = {
            @CacheEvict(value = "movies", allEntries = true),
            @CacheEvict(cacheNames = "movierating", allEntries = true)
    })
    public void bulkDelete(List<Long> ids) {
        movieRepository.deleteAllById(ids);
    }
}
