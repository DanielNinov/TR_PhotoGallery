package teamRocketPhotoGallery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import teamRocketPhotoGallery.bindingModel.PhotoBindingModel;
import teamRocketPhotoGallery.entity.Photo;
import teamRocketPhotoGallery.entity.User;
import teamRocketPhotoGallery.repository.PhotoRepository;
import teamRocketPhotoGallery.repository.UserRepository;

import javax.persistence.Transient;

@Controller
public class PhotoController {
    @Autowired
    private PhotoRepository photoRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/article/create")
    @PreAuthorize("isAuthenticated()")
    public String create(Model model) {
        model.addAttribute("view", "/article/create");
        return "base-layout";
    }

    @PostMapping("/article/create")
    @PreAuthorize("isAuthenticated()")
    public String createProcess(PhotoBindingModel photoBindingModel) {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userEntity = this.userRepository.findByEmail(user.getUsername());
        Photo photoEntity = new Photo(photoBindingModel.getTitle(), photoBindingModel.getContent(), userEntity);
        this.photoRepository.saveAndFlush(photoEntity);
        return "redirect:/";
    }

    @GetMapping("/article/{id}")
    public String details(Model model, @PathVariable Integer id) {
        if (!this.photoRepository.exists(id)) {
            return "redirect:/";
        }
        if (!(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
            UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User entityUser = this.userRepository.findByEmail(principal.getUsername());
            model.addAttribute("user", entityUser);
        }
        Photo photo = this.photoRepository.findOne(id);
        model.addAttribute("article", photo);
        model.addAttribute("view", "photo/details");
        return "base-layout";
    }

    @GetMapping("/article/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String edit(@PathVariable Integer id, Model model) {
        if (!this.photoRepository.exists(id)) {
            return "redirect:/";
        }
        Photo photo = this.photoRepository.findOne(id);
        if (!isUserAuthorOrAdmin(photo)) {
            return "redirect:/photo/" + id;
        }
        model.addAttribute("article", photo);
        model.addAttribute("view", "photo/edit");
        return "base-layout";
    }

    @PostMapping("/article/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String editProcess(@PathVariable Integer id, PhotoBindingModel photoBindingModel) {
        if (!this.photoRepository.exists(id)) {
            return "redirect:/";
        }
        Photo photo = this.photoRepository.findOne(id);
        if (!isUserAuthorOrAdmin(photo)) {
            return "redirect:/photo/" + id;
        }
        photo.setContent(photoBindingModel.getContent());
        photo.setTitle(photoBindingModel.getTitle());
        this.photoRepository.saveAndFlush(photo);
        return "redirect:/photo/" + id;
    }

    @GetMapping("/article/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String delete(@PathVariable Integer id, Model model) {
        if (!this.photoRepository.exists(id)) {
            return "redirect:/";
        }
        Photo photo = this.photoRepository.findOne(id);
        if (!isUserAuthorOrAdmin(photo)) {
            return "redirect:/photo/" + id;
        }
        model.addAttribute("article", photo);
        model.addAttribute("view", "photo/delete");
        return "base-layout";
    }

    @PostMapping("/article/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String deleteProcess(@PathVariable Integer id, PhotoBindingModel photoBindingModel) {
        if (!this.photoRepository.exists(id)) {
            return "redirect:/";
        }
        Photo photo = this.photoRepository.findOne(id);
        if (!isUserAuthorOrAdmin(photo)) {
            return "redirect:/photo/" + id;
        }
        this.photoRepository.delete(photo);
        return "redirect:/";
    }

    @Transient
    public boolean isUserAuthorOrAdmin(Photo photo) {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userEntity = this.userRepository.findByEmail(user.getUsername());
        return userEntity.isAdmin() || userEntity.isAuthor(photo);
    }
}
