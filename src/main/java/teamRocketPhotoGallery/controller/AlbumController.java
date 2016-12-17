package teamRocketPhotoGallery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.util.StringUtils;
import teamRocketPhotoGallery.bindingModel.AlbumBindingModel;
import teamRocketPhotoGallery.entity.Album;
import teamRocketPhotoGallery.entity.Category;
import teamRocketPhotoGallery.entity.Photo;
import teamRocketPhotoGallery.entity.User;
import teamRocketPhotoGallery.repository.AlbumRepository;
import teamRocketPhotoGallery.repository.CategoryRepository;
import teamRocketPhotoGallery.repository.PhotoRepository;
import teamRocketPhotoGallery.repository.UserRepository;

import javax.persistence.Transient;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Daniel on 12-Dec-16.
 */
@Controller
@RequestMapping("albums")
public class AlbumController {
    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/")
    public String list(Model model){
        model.addAttribute("view", "album/list");

        List<Album> albums = this.albumRepository.findAll();

        albums = albums.stream().sorted(Comparator.comparingInt(Album::getId)).collect(Collectors.toList());

        model.addAttribute("albums", albums);

        return "base-layout";
    }

    @GetMapping("/create")
    public String create(Model model){
        model.addAttribute("view", "album/create");

        List<Category> categories = this.categoryRepository.findAll();

        model.addAttribute("categories", categories);

        return "base-layout";
    }

    @PostMapping("/create")
    public String createProcess(AlbumBindingModel albumBindingModel, RedirectAttributes redirectAttributes){
        if(StringUtils.isEmpty(albumBindingModel.getName())){
            redirectAttributes.addFlashAttribute("error", "The form must not be empty");
            return "redirect:/albums/create";
        }

        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userEntity = this.userRepository.findByEmail(user.getUsername());
        Category category = this.categoryRepository.findOne(albumBindingModel.getCategoryId());

        Album album = new Album(albumBindingModel.getName(), userEntity,category);

        this.albumRepository.saveAndFlush(album);
        return "redirect:/albums/";
    }

    @GetMapping("/delete/{id}")
    public String delete(Model model, @PathVariable Integer id){
        if(!this.albumRepository.exists(id)){
            return "redirect:/albums/";
        }

        Album album = this.albumRepository.findOne(id);

        if (!isUserAlbumAuthorOrAdmin(album)) {
            return "redirect:/albums/";
        }

        model.addAttribute("album", album);
        model.addAttribute("view", "album/delete");

        return "base-layout";
    }

    @PostMapping("/delete/{id}")
    public String deleteProcess(@PathVariable Integer id) {
        if(!this.albumRepository.exists(id)){
            return "redirect:/albums/";
        }

        Album album = this.albumRepository.findOne(id);

        for(Photo photo : album.getPhotos()){
            this.photoRepository.delete(photo);
        }

        this.albumRepository.delete(album);

        return "redirect:/albums/";
    }

    @Transient
    public boolean isUserAlbumAuthorOrAdmin(Album album) {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userEntity = this.userRepository.findByEmail(user.getUsername());
        return userEntity.isAdmin() || userEntity.isAlbumAuthor(album);
    }
}
