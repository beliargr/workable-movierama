package com.workable.movierama.repositories;

import com.workable.movierama.models.entities.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    List<Rating> findAllByCreatedByAndMovie_Id(String createdBy, Long movieId);

}
