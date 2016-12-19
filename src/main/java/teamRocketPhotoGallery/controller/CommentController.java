package teamRocketPhotoGallery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import teamRocketPhotoGallery.bindingModel.CommentBindingModel;
import teamRocketPhotoGallery.bindingModel.PhotoBindingModel;
import teamRocketPhotoGallery.entity.Comment;
import teamRocketPhotoGallery.entity.Photo;
import teamRocketPhotoGallery.entity.User;
import teamRocketPhotoGallery.repository.CommentRepository;
import teamRocketPhotoGallery.repository.PhotoRepository;
import teamRocketPhotoGallery.repository.UserRepository;

/**
 * Created by petar on 15/12/2016.
 */
@Controller
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PhotoRepository photoRepository;

    @GetMapping("/comment/create/{id}")
    @PreAuthorize("isAuthenticated()")
    public String create(@PathVariable Integer id, Model model){
        model.addAttribute("id", id);
        model.addAttribute("view", "/comment/create");


        return "base-layout";

    }
    @PostMapping("/comment/create/{id}")
    @PreAuthorize("isAuthenticated()")
    public String createProcess(CommentBindingModel commentBindingModel, @PathVariable Integer id){

        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User userEntity = this.userRepository.findByEmail(user.getUsername());

        Photo photo = this.photoRepository.findOne(id);

        Comment commentEntity = new Comment(
                commentBindingModel.getContent(),
                userEntity,
                photo
        );

        this.commentRepository.saveAndFlush(commentEntity);

        return "redirect:/";

    }
}
