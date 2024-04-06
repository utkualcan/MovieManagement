package org.kurgu.moviemanagement.Controllers;

import org.kurgu.moviemanagement.Repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "index"; // Assuming index.html exists in templates folder
    }
}
