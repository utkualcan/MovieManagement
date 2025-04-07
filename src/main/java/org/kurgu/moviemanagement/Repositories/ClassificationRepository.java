package org.kurgu.moviemanagement.Repositories;

import org.kurgu.moviemanagement.Models.Classification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ClassificationRepository extends JpaRepository<Classification, Integer> {

    Optional<Classification> findByMovieIdAndCategoryIdAndIsdeletedFalse(int movieId, int categoryId);

    List<Classification> findByIsdeletedFalse();

    @Query("SELECT c FROM classification c WHERE c.classificationId = :id AND c.isdeleted = false")
    Optional<Classification> findActiveById(@Param("id") int classificationId);

}