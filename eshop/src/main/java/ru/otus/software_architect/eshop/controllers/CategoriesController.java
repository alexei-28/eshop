package ru.otus.software_architect.eshop.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.otus.software_architect.eshop.model.Category;
import ru.otus.software_architect.eshop.model.ViewMode;
import ru.otus.software_architect.eshop.repo.CategoryRepository;

import java.util.Date;
import java.util.Optional;

@Controller
public class CategoriesController {
    @Value("${spring.application.name}")
    private String appName;

    private CategoryRepository categoryRepository;

    private static Logger logger = LogManager.getLogger(CategoriesController.class);

    // If class has only one constructor then @Autowired wiil execute automatically
    public CategoriesController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
        //createStubCategoryList();
    }

    @GetMapping("/categories")
    public String getAllCategories(Model model) {
        model.addAttribute("categoriesList", categoryRepository.findAll());
        model.addAttribute("appName", appName);
        return "categories";
    }

    @RequestMapping("category/add")
    public String addtCategory(Model model) {
        logger.info("addCategory");
        model.addAttribute("isAdd", true);
        model.addAttribute("category", new Category());
        model.addAttribute("title", "Add Category");
        model.addAttribute("viewMode", ViewMode.ADD);
        return "category";
    }

    @RequestMapping("category/view/{id}")
    public String viewCategory(@PathVariable("id") int id, Model model) {
        Optional<Category> category = categoryRepository.findById(id);
        logger.info("find_category = " + category);
        model.addAttribute("isView", true);
        model.addAttribute("category", category);
        model.addAttribute("title", "View Category");
        return "category";
    }

    @RequestMapping("category/edit/{id}")
    public String editCategory(@PathVariable("id") int id, Model model) {
        Optional<Category> category = categoryRepository.findById(id);
        logger.info("find_category = " + category);
        model.addAttribute("category", category);
        model.addAttribute("title", "Edit Category");
        model.addAttribute("viewMode", ViewMode.EDIT);
        return "category";
    }

    @RequestMapping("category/delete/{id}")
    public String deleteCategory(@PathVariable("id") int id) {
        categoryRepository.deleteById(id);
        return "redirect:/categories";
    }

    @PostMapping(value = "/category")
    public String submitCategory(Category category, Model model) {
        logger.info("submitCategory = " + category);
        if (category.getId() == 0) { // add category
            category.setCreated(new Date());
        } else { // update category
            category.setUpdated(new Date());
        }
        categoryRepository.save(category);
        return "redirect:/categories";
    }

}
