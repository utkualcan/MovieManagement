package org.kurgu.moviemanagement.Repositories;


import org.kurgu.moviemanagement.Models.Classification;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ClassificationRepository extends JpaRepository<Classification, Integer> {
}
