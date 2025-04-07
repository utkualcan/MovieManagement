package org.kurgu.moviemanagement.DTOs;

import lombok.*;
import org.kurgu.moviemanagement.Models.Classification;
import org.kurgu.moviemanagement.Models.Movie;
import org.kurgu.moviemanagement.Models.Category;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassificationResponseDTO {

    private int classificationId;
    private Movie movie;
    private Category category;
    private LocalDate date;

    public static ClassificationResponseDTO fromEntities(Classification classification, Movie movie, Category category) {
        return new ClassificationResponseDTO(
                classification.getClassificationId(),
                movie,
                category,
                classification.getDate()
        );
    }
}