package com.workable.movierama.services;

import com.workable.movierama.models.dto.movie.CreateMovieDto;
import com.workable.movierama.models.dto.movie.MovieDto;
import com.workable.movierama.models.dto.movie.UpdateMovieDto;
import com.workable.movierama.models.dto.movierating.MovieRatingResultSet;
import com.workable.movierama.models.entities.Movie;
import com.workable.movierama.models.enums.MovieGenre;
import com.workable.movierama.models.enums.RatingType;
import com.workable.movierama.repositories.MovieRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class MovieServiceTest {

    private MovieRepository movieRepository;
    private SecurityService securityService;
    private MovieService movieService;

    @BeforeEach
    void setUp() {
        movieRepository = mock(MovieRepository.class);
        securityService = mock(SecurityService.class);
        movieService = new MovieService(movieRepository, securityService);
    }

    @Test
    void create_shouldSaveAndReturnMovie() {
        CreateMovieDto dto = new CreateMovieDto("Title", "Description", MovieGenre.ACTION);
        Movie savedMovie = new Movie();
        when(movieRepository.save(any(Movie.class))).thenReturn(savedMovie);

        Movie result = movieService.create(dto);

        assertThat(result).isEqualTo(savedMovie);
        verify(movieRepository).save(any(Movie.class));
    }

    @Test
    void findById_shouldReturnMovieIfExists() {
        Movie movie = new Movie();
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));

        Optional<Movie> result = movieService.findById(1L);

        assertThat(result).contains(movie);
    }

    @Test
    void getMovie_shouldReturnMappedDto() {
        Movie movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Test");
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));

        Optional<MovieDto> result = movieService.getMovie(1L);

        assertThat(result).isPresent();
        assertThat(result.get().id()).isEqualTo(1L);
        assertThat(result.get().title()).isEqualTo("Test");
    }

    @Test
    void findAll_shouldReturnAllMovies() {
        List<Movie> movies = List.of(new Movie(), new Movie());
        when(movieRepository.findAll()).thenReturn(movies);

        List<Movie> result = movieService.findAll();

        assertThat(result).hasSize(2);
    }

    @Test
    void getMovies_shouldReturnMappedDtos() {
        Movie movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Test");
        when(movieRepository.findAll()).thenReturn(List.of(movie));

        List<MovieDto> result = movieService.getMovies();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).title()).isEqualTo("Test");
    }

    @Test
    void getMoviesWithRatings_shouldReturnMappedDtos() {
        MovieRatingResultSet resultSet = new MovieRatingResultSet(1L, "Title", "Desc", (short) 0, "user", Instant.now(), Instant.now(), 10L, 10L, (short) 0, 1L);
        when(securityService.getUsername()).thenReturn("user");
        when(movieRepository.getMovieRatingsByUser("user")).thenReturn(List.of(resultSet));

        var result = movieService.getMoviesWithRatings();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).title()).isEqualTo("Title");
        assertThat(result.get(0).genre()).isEqualTo(MovieGenre.ACTION);
        assertThat(result.get(0).userRating()).isEqualTo(RatingType.LIKE);
    }

    @Test
    void update_shouldModifyAndSaveMovie() {
        Movie movie = new Movie();
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(movieRepository.save(movie)).thenReturn(movie);

        UpdateMovieDto dto = new UpdateMovieDto("Title", "Desc", MovieGenre.ACTION);
        Movie result = movieService.update(1L, dto);

        assertThat(result.getTitle()).isEqualTo("Title");
        assertThat(result.getDescription()).isEqualTo("Desc");
        assertThat(result.getGenre()).isEqualTo(MovieGenre.ACTION);
        verify(movieRepository).save(movie);
    }

    @Test
    void update_shouldThrowIfMovieNotFound() {
        when(movieRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> movieService.update(1L, new UpdateMovieDto("t", "d", MovieGenre.ACTION)))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void delete_shouldRemoveMovie() {
        Movie movie = new Movie();
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));

        movieService.delete(1L);

        verify(movieRepository).delete(movie);
    }

    @Test
    void delete_shouldThrowIfMovieNotFound() {
        when(movieRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> movieService.delete(1L))
                .isInstanceOf(EntityNotFoundException.class);
    }
}
