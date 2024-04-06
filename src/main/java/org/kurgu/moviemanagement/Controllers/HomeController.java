package org.kurgu.moviemanagement.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "index"; // Assuming index.html exists in templates folder
    }

    @GetMapping("/movie")
    public String movie() {
        return "movies/index"; // Assuming movie/index.html exists in templates folder
    }

    @GetMapping("/category")
    public String category() {
        return "category/index"; // Assuming category/index.html exists in templates folder
    }

    @GetMapping("/classification")
    public String classification() {
        return "classification/index"; // Assuming classification/index.html exists in templates folder
    }
}
