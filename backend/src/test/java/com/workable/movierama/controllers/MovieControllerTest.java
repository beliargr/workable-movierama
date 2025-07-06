package com.workable.movierama.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.workable.movierama.models.dto.movie.CreateMovieDto;
import com.workable.movierama.models.dto.movie.MovieDto;
import com.workable.movierama.models.dto.movie.UpdateMovieDto;
import com.workable.movierama.models.dto.movierating.MovieRatingDto;
import com.workable.movierama.models.entities.Movie;
import com.workable.movierama.models.enums.MovieGenre;
import com.workable.movierama.models.enums.RatingType;
import com.workable.movierama.services.MovieService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MovieController.class)
@AutoConfigureMockMvc(addFilters = false)
class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MovieService movieService;


    @Test
    @DisplayName("GET by id /movies/{id} - Found")
    void getById_found() throws Exception {

        Instant now = Instant.now();

        String nowStr = DateTimeFormatter.ISO_INSTANT.format(now);

        MovieDto dto = new MovieDto(1L, "Title", "Desc", MovieGenre.ACTION, "user", now, now);
        Mockito.when(movieService.getMovie(1L)).thenReturn(Optional.of(dto));

        mockMvc.perform(get("/movies/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Title"))
                .andExpect(jsonPath("$.description").value("Desc"))
                .andExpect(jsonPath("$.genre").value("ACTION"))
                .andExpect(jsonPath("$.createdBy").value("user"))
                .andExpect(jsonPath("$.createdDate").value(nowStr))
                .andExpect(jsonPath("$.lastModifiedDate").value(nowStr));
    }

    @Test
    @DisplayName("GET by id  /movies/{id} - Not Found")
    void getById_notFound() throws Exception {
        Mockito.when(movieService.getMovie(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/movies/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /movies - All Movies")
    void getAll() throws Exception {

        Instant now = Instant.now();
        List<MovieDto> movies = List.of(
                new MovieDto(1L, "Title", "Desc", MovieGenre.ACTION, "user", now, now),
                new MovieDto(2L, "Title", "Desc", MovieGenre.ACTION, "user", now, now)
        );
        Mockito.when(movieService.getMovies()).thenReturn(movies);

        mockMvc.perform(get("/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("GET /movies/ratings - With Ratings")
    void getMoviesWithRatings() throws Exception {
        Instant now = Instant.now();
        String nowStr = DateTimeFormatter.ISO_INSTANT.format(now);
        List<MovieRatingDto> ratings = List.of(
                new MovieRatingDto(1L, "Title", "Desc", MovieGenre.ACTION, "user", now, now, 10L, 10L, RatingType.LIKE, 1L)
        );
        Mockito.when(movieService.getMoviesWithRatings()).thenReturn(ratings);

        mockMvc.perform(get("/movies/ratings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Title"))
                .andExpect(jsonPath("$.description").value("Desc"))
                .andExpect(jsonPath("$.genre").value("ACTION"))
                .andExpect(jsonPath("$.createdBy").value("user"))
                .andExpect(jsonPath("$.createdDate").value(nowStr))
                .andExpect(jsonPath("$.lastModifiedDate").value(nowStr))
                .andExpect(jsonPath("$.likes").value(10))
                .andExpect(jsonPath("$.hates").value(10))
                .andExpect(jsonPath("$.userRating").value("LIKE"))
                .andExpect(jsonPath("$.ratingId").value(1L));
    }

    @Test
    @DisplayName("POST /movies - Create Movie")
    void create() throws Exception {

        Instant now = Instant.now();
        String nowStr = DateTimeFormatter.ISO_INSTANT.format(now);
        CreateMovieDto createDto = new CreateMovieDto("Title", "Desc", MovieGenre.ACTION);
        Movie movie = new Movie();
        movie.setTitle("Title");
        movie.setDescription("Desc");
        movie.setGenre(MovieGenre.ACTION);
        movie.setCreatedBy("user");
        movie.setCreatedDate(now);
        movie.setLastModifiedDate(now);
        MovieDto movieDto = new MovieDto(movie);
        Mockito.when(movieService.create(any(CreateMovieDto.class)))
                .thenReturn(movie);

        mockMvc.perform(post("/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(movieDto.title()))
                .andExpect(jsonPath("$.description").value(movieDto.description()))
                .andExpect(jsonPath("$.genre").value(movieDto.genre().toString()))
                .andExpect(jsonPath("$.createdBy").value(movieDto.createdBy()))
                .andExpect(jsonPath("$.createdDate").value(DateTimeFormatter.ISO_INSTANT.format(movieDto.createdDate())))
                .andExpect(jsonPath("$.lastModifiedDate").value(DateTimeFormatter.ISO_INSTANT.format(movieDto.lastModifiedDate())));
    }

    @Test
    @DisplayName("PATCH /movies/{id} - Update Movie")
    void update() throws Exception {
        Instant now = Instant.now();
        String nowStr = DateTimeFormatter.ISO_INSTANT.format(now);

        Movie movie = new Movie();
        movie.setTitle("Updated Title");
        movie.setDescription("Updated Desc");
        movie.setGenre(MovieGenre.ACTION);
        movie.setCreatedBy("user");
        movie.setCreatedDate(now);
        movie.setLastModifiedDate(now);
        MovieDto movieDto = new MovieDto(movie);


        UpdateMovieDto updateDto = new UpdateMovieDto("Updated Title", "Updated Desc", MovieGenre.ACTION);
        MovieDto updatedDto = new MovieDto(movie);

        Mockito.when(movieService.update(eq(1L), any(UpdateMovieDto.class))).thenReturn(movie);

        mockMvc.perform(patch("/movies/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(movieDto.title()))
                .andExpect(jsonPath("$.description").value(movieDto.description()))
                .andExpect(jsonPath("$.genre").value(movieDto.genre().toString()))
                .andExpect(jsonPath("$.createdBy").value(movieDto.createdBy()))
                .andExpect(jsonPath("$.createdDate").value(DateTimeFormatter.ISO_INSTANT.format(movieDto.createdDate())))
                .andExpect(jsonPath("$.lastModifiedDate").value(DateTimeFormatter.ISO_INSTANT.format(movieDto.lastModifiedDate())));
    }

    @Test
    @DisplayName("DELETE /movies/{id} - Delete Movie")
    void testDelete() throws Exception {
        mockMvc.perform(delete("/movies/1"))
                .andExpect(status().isNoContent());
        Mockito.verify(movieService).delete(1L);
    }
}
