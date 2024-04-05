package org.kurgu.moviemanagement.Repositories;

import org.kurgu.moviemanagement.Models.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Integer> {
}
