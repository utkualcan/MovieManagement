package org.kurgu.moviemanagement.Controllers;

import org.kurgu.moviemanagement.Models.Movie;
import org.kurgu.moviemanagement.Repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MovieController {
    @Autowired
    private final MovieRepository movieRepository;

    public MovieController(MovieRepository movieRepository) {this.movieRepository = movieRepository;}
    @GetMapping("/movie")
    public String getMovies(Model model) {
        Iterable<Movie> movies = movieRepository.findAll();
        model.addAttribute("movies", movies);
        return "movie/index";
    }
}