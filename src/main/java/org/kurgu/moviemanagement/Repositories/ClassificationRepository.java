package org.kurgu.moviemanagement.Repositories;

import org.kurgu.moviemanagement.DTOs.ClassificationViewModel;
import org.kurgu.moviemanagement.Models.Classification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ClassificationRepository extends CrudRepository<Classification, Integer> {
    @Query("SELECT c from classification c WHERE c.isDeleted = false")
    Iterable<Classification> getAll();

    @Modifying
    @Transactional
    @Query("UPDATE classification c SET c.isDeleted= true WHERE c.classification_id = :id")
    void setDeleted(@Param("id") int id);

    @Modifying
    @Transactional
    @Query("UPDATE classification c SET c.isReturned= true WHERE c.id = :id")
    void setReturned(@Param("id") int id);
}
