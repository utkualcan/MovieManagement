package org.kurgu.moviemanagement.Repositories;

import org.kurgu.moviemanagement.Models.Movie;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MovieRepository extends CrudRepository<Movie, Integer> {
    Optional<Movie> findByTitle(String title);
}
