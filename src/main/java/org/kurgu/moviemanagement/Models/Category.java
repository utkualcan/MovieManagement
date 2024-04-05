package org.kurgu.moviemanagement.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name="category")

public class Category {
    @Id
    public int category_id;

    public String name;
}
