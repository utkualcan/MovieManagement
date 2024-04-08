package org.kurgu.moviemanagement.Repositories;

import org.kurgu.moviemanagement.Models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Integer> {
}
