package org.kurgu.moviemanagement.Models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name= "templates/classification")

public class Classification {
    @Id
    private int classification_id;

    @Column(name="movie_id")
    private int movieId;

    @Column(name="category_id")
    private int categoryId;

    @Column(name="date")
    private Date date;
}
