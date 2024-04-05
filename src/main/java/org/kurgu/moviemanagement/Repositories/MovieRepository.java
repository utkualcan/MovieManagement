package org.kurgu.moviemanagement.Repositories;

import org.kurgu.moviemanagement.Models.Movie;
import org.springframework.data.repository.CrudRepository;

public interface MovieRepository extends CrudRepository<Movie, Integer> {
}
