package org.kurgu.moviemanagement.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name="movie")

public class Movie {

    @Id
    private int movie_id;

    private String title;

    private String director;

    private int year;

}
