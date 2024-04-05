package org.kurgu.moviemanagement.Controllers;

import org.kurgu.moviemanagement.Models.Category;
import org.kurgu.moviemanagement.Repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class CategoryController {
    @Autowired
    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {this.categoryRepository = categoryRepository;}

    @GetMapping("/category")
    public String getIndex(Model model) {
        Iterable<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        return "category/index";
    }

    @GetMapping("/category/add")
    public String addCategory(Model model) {
        Category category = new Category();
        model.addAttribute("category", category);
        return "category/addCategory";
    }

    @PostMapping("/category/add")
    public String addCategory(@ModelAttribute("category") Category category) {
        categoryRepository.save(category);
        return "redirect:/category";
    }
    @GetMapping("/category/update/{cid}")
    public String updateCategory(Model model, @PathVariable int cid) {
        Optional<Category> category = categoryRepository.findById(cid);
        model.addAttribute("category", category.get());
        return "category/updateCategory";
    }

    @PostMapping("/category/update")
    public String categoryUpdate(@ModelAttribute("category") Category category) {
        categoryRepository.save(category);
        return "redirect:/category";
    }

    @PostMapping("/category/delete/{cid}")
    public String deleteCategory(@PathVariable int cid) {
        categoryRepository.deleteById(cid);
        return "redirect:/category";
    }
}
