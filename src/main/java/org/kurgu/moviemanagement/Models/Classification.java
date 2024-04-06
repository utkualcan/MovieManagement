package org.kurgu.moviemanagement.Models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name= "classification")

public class Classification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int classification_id;

    @Column(name="movie_id")
    private int movieId;

    @Column(name="category_id")
    private int categoryId;

    @Column(name="date")
    private Date date;

}
