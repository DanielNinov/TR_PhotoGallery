package teamRocketPhotoGallery.controller.admin;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import teamRocketPhotoGallery.bindingModel.CategoryBindingModel;
import teamRocketPhotoGallery.entity.Category;
import teamRocketPhotoGallery.repository.CategoryRepository;
import teamRocketPhotoGallery.repository.PhotoRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/admin/categories")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private PhotoRepository photoRepository;

    @GetMapping("/")
    public String list(Model model){
        model.addAttribute("view", "admin/category/list");
        List<Category> categories = this.categoryRepository.findAll();
        categories =categories.stream()
                .sorted(Comparator.comparingInt(Category::getId))
                .collect(Collectors.toList());
        model.addAttribute("categories", categories);

        return "base-layout";
    }
    @GetMapping("/create")
    public String create(Model model){
        model.addAttribute("view", "/admin/category/create");

        return "base-layout";
    }
    @PostMapping("/create")
    public String createProcess(CategoryBindingModel categoryBindingModel){


        Category category = new Category(categoryBindingModel.getName());
        this.categoryRepository.saveAndFlush(category);

        return "redirect:/admin/categories/";

    }
}
