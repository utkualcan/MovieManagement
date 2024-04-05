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
        return "category/addCategory";
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
    @GetMapping("/category/del")
    public String deleteCategory(Model model,@RequestParam("id") int id) {
        Optional<Category> category = categoryRepository.findById(id);
        model.addAttribute("category", category);
        return "category/delcategory";
    }
    @PostMapping("/category/del")
    public String categoryDelete(@ModelAttribute("category") Category category) {
        if(category==null){
            System.out.println("Category is null");
            return "redirect:/category";
        }
        categoryRepository.delete(category);
        return "redirect:/category";
    }
    @GetMapping("/category/update")
    public String updateCategory(Model model, @RequestParam("id") int id) {
        Optional<Category> category = categoryRepository.findById(id);
        model.addAttribute("category", category);
        return "/category/updateCategory";
    }

    @PostMapping("/category/update")
    public String categoryUpdate(Category category) {
        if(category==null){
            System.out.println("Category is null");
            return "redirect:/category";
        }
        categoryRepository.save(category);
        return "redirect:/category";
    }
}
