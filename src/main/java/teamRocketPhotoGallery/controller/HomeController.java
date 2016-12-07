package teamRocketPhotoGallery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import teamRocketPhotoGallery.entity.Photo;
import teamRocketPhotoGallery.repository.PhotoRepository;

import java.util.List;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index(Model model) {
        List<Photo> photos = this.photoRepository.findAll();
        model.addAttribute("view", "home/index");
        model.addAttribute("photos", photos);
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
