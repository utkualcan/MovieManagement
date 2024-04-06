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

    public int getClassification_id() {
        return classification_id;
    }

    public void setClassification_id(int classification_id) {
        this.classification_id = classification_id;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
