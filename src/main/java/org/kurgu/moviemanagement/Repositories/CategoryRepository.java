package org.kurgu.moviemanagement.Repositories;

import org.kurgu.moviemanagement.Models.Category;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
