package org.kurgu.moviemanagement.Controllers;

import org.kurgu.moviemanagement.DTOs.ClassificationViewModel;
import org.kurgu.moviemanagement.Models.Category;
import org.kurgu.moviemanagement.Models.Classification;
import org.kurgu.moviemanagement.Models.Movie;
import org.kurgu.moviemanagement.Repositories.CategoryRepository;
import org.kurgu.moviemanagement.Repositories.ClassificationRepository;
import org.kurgu.moviemanagement.Repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.View;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class ClassificationController {
    @Autowired
    ClassificationRepository classificationRepository;

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    private View error;


    @GetMapping("/classification")
    public String getClassifications(Model model) {
        Iterable<Classification> classifications = classificationRepository.findAll();
        List<ClassificationViewModel> classificationViews = new ArrayList<>();
        for (Classification c : classifications) {
            if(c.isIsdeleted() == false){
                ClassificationViewModel cv = new ClassificationViewModel();
                cv.setCategory(categoryRepository.findById(c.getCategoryId()).get());
                cv.setMovie(movieRepository.findById(c.getMovieId()).get());
                cv.setClassification(c);
                cv.setDate(c.getDate());
                classificationViews.add(cv);
            }
        }
        model.addAttribute("serhat", classificationViews);
        return "/classification/index";
    }
    @GetMapping("/classification/update/{id}")
    public String updateClassificationForm(@PathVariable("id") int id, Model model) {
        Optional<Classification> optionalClassification = classificationRepository.findById(id);
        Iterable<Movie> movies = movieRepository.findAll();
        Iterable<Category> categories = categoryRepository.findAll();
        if (optionalClassification.isPresent()) {
            model.addAttribute("serhat", optionalClassification.get());
            model.addAttribute("movies", movies);
            model.addAttribute("category", categories);
            return "/classification/updateclassification";
        } else {
            return "redirect:/classification";
        }
    }

    @PostMapping("/classification/update/{id}")
    public String updateClassification(@PathVariable("id") int id, @ModelAttribute Classification classification) {
        classification.setClassification_id(id);
        Iterable<Classification> classifications = classificationRepository.findAll();
        for(Classification c : classifications)
        {
            if(c.getMovieId() == classification.getMovieId() && c.getCategoryId() == classification.getCategoryId())
            {
                return "redirect:/classification";
            }

        }
        classificationRepository.save(classification);
        return "redirect:/classification";
    }

    @GetMapping("classification/classificatecategory/{id}")
    public String classificateCategory(@PathVariable("id") int id, Model model){
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        Iterable<Movie> movies = movieRepository.findAll();
        Classification classification = new Classification();
        classification.setCategoryId(optionalCategory.get().getCategory_id());
        if (optionalCategory.isPresent()) {
            model.addAttribute("category", optionalCategory.get());
            model.addAttribute("movies", movies);
            model.addAttribute("classificate",classification);
            return "/classification/classificatecategory";
        } else {
            return "redirect:/classification";
        }
    }
    @PostMapping("classification/classificatecategory/{id}")
    public String classificateCategory(@PathVariable("id") int id, @ModelAttribute Classification classificate){
        classificate.setClassification_id(id);
        Iterable<Classification> classifications = classificationRepository.findAll();
        for(Classification c : classifications)
        {
            if(c.getMovieId() == classificate.getMovieId() && c.getCategoryId() == classificate.getCategoryId())
            {
                return "redirect:/classification";
            }

        }
        classificationRepository.save(classificate);
        return "redirect:/classification";
    }
    @GetMapping("classification/classificatemovie/{id}")
    public String classificateMovie(@PathVariable("id") int id, Model model){
        Optional<Movie> optionalMovie = movieRepository.findById(id);
        Iterable<Category> categories = categoryRepository.findAll();
        Classification classification = new Classification();
        classification.setMovieId(optionalMovie.get().getMovie_id());
        if (optionalMovie.isPresent()) {
            model.addAttribute("movies", optionalMovie.get());
            model.addAttribute("categories", categories);
            model.addAttribute("classificate",classification);
            return "/classification/classificatemovie";
        } else {
            return "redirect:/classification";
        }
    }
    @PostMapping("classification/classificatemovie/{id}")
    public String classificateMovie(@PathVariable("id") int id, @ModelAttribute Classification classificate){
        classificate.setClassification_id(id);
        Iterable<Classification> classifications = classificationRepository.findAll();
        for(Classification c : classifications)
        {
            if(c.getMovieId() == classificate.getMovieId() && c.getCategoryId() == classificate.getCategoryId())
            {
                return "redirect:/classification";
            }

        }
        classificationRepository.save(classificate);
        return "redirect:/classification";
    }

    @GetMapping("/classification/delete/{id}")
    public String deleteClassification(@PathVariable("id") int id) {

        Optional<Classification> optionalClassification = classificationRepository.findById(id);
        optionalClassification.get().setIsdeleted(true);
        classificationRepository.save(optionalClassification.get());

        return "redirect:/classification";
    }
}
