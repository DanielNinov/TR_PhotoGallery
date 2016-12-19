package teamRocketPhotoGallery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import teamRocketPhotoGallery.entity.Tag;
import teamRocketPhotoGallery.repository.TagRepository;

/**
 * Created by Daniel on 19-Dec-16.
 */
@Controller
public class TagController {
    @Autowired
    private TagRepository tagRepository;

    @GetMapping("/tag/{name}")
    public  String photosWithTag(Model model, @PathVariable String name){
        Tag tag = this.tagRepository.findByName(name);

        if(tag == null){
            return "redirect:/";
        }

        model.addAttribute("view", "tag/photos");
        model.addAttribute("tag", tag);

        return "base-layout";
    }
}
