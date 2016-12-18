package teamRocketPhotoGallery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import teamRocketPhotoGallery.entity.Category;
import teamRocketPhotoGallery.entity.Photo;
import teamRocketPhotoGallery.repository.CategoryRepository;
import teamRocketPhotoGallery.repository.PhotoRepository;

import java.util.List;
import java.util.Set;

@Controller
public class HomeController {
    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/")
    public String index(Model model) {
        List<Category> categories = this.categoryRepository.findAll();
        model.addAttribute("view", "home/index");
        model.addAttribute("categories", categories);
        return "base-layout";
    }

    @Autowired
    private PhotoRepository photoRepository;

    @GetMapping("/error/403")
    public String accessDenied (Model model){
        model.addAttribute("view", "/error/403");
        return "base-layout";
    }
}
