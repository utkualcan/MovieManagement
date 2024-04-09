package org.kurgu.moviemanagement.DTOs;

import lombok.*;
import org.kurgu.moviemanagement.Models.Classification;
import org.kurgu.moviemanagement.Models.Movie;
import org.kurgu.moviemanagement.Models.Category;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassificationViewModel {

    private Movie movie;
    private Category category;
    private Classification classification;
    private LocalDate date = LocalDate.now();


}
