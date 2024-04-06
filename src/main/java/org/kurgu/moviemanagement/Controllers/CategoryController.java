package org.kurgu.moviemanagement.Controllers;

import org.kurgu.moviemanagement.Models.Category;
import org.kurgu.moviemanagement.Repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        return "category/index";
    }

    @PostMapping("/category/add")
    public String addCategory(@ModelAttribute("category") Category category) {
        if(category==null){
            System.out.println("Category is null");
            return "redirect:/category";
        }
        categoryRepository.save(category);
        return "redirect:/category";
    }
    @GetMapping("/category/update/{id}")
    public String updateCategoryForm(@PathVariable("id") int id, Model model) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isPresent()) {
            model.addAttribute("category", optionalCategory.get());
            return "category/updatecategory";
        } else {
            return "redirect:/category";
        }
    }

    @PostMapping("/category/update/{id}")
    public String updateCategory(@PathVariable("id") int id, @ModelAttribute Category category) {
        category.setCategory_id(id);
        categoryRepository.save(category);
        return "redirect:/category";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") int id) {
        categoryRepository.deleteById(id);
        return "redirect:/category";
    }


}
