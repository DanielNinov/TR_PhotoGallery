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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.util.StringUtils;
import teamRocketPhotoGallery.bindingModel.PhotoBindingModel;

import teamRocketPhotoGallery.entity.*;

import teamRocketPhotoGallery.repository.*;

import javax.persistence.Transient;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class PhotoController {
    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TagRepository tagRepository;

    @GetMapping("/category/{id}")
    public String listPhotos(Model model, @PathVariable Integer id) {
        Set<Photo> photos = this.categoryRepository.findOne(id).getPhotos();

        model.addAttribute("view", "photo/list-photos");
        model.addAttribute("photos", photos);

        return "base-layout";
    }

    @GetMapping("/photo/upload")
    @PreAuthorize("isAuthenticated()")
    public String upload(Model model) {
        model.addAttribute("view", "/photo/upload");

        List<Album> albums = this.albumRepository.findAll();
        model.addAttribute("albums", albums);

        List<Category> categories = this.categoryRepository.findAll();
        model.addAttribute("categories", categories);

        return "base-layout";
    }

    @PostMapping("/photo/upload")
    @PreAuthorize("isAuthenticated()")
    public String uploadProcess(PhotoBindingModel photoBindingModel) {
        if (StringUtils.isEmpty(photoBindingModel.getTitle()) || StringUtils.isEmpty(photoBindingModel.getContent())) {
            return "redirect:/photo/upload";
        }
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userEntity = this.userRepository.findByEmail(user.getUsername());
        Album album = this.albumRepository.findOne(photoBindingModel.getAlbumId());
        Category category = this.categoryRepository.findOne(photoBindingModel.getCategoryId());
        HashSet<Tag> tags = this.findTagsFromString(photoBindingModel.getTagString());

        Photo photoEntity = new Photo(photoBindingModel.getTitle(),
                                        photoBindingModel.getContent(),
                                        userEntity,
                                        album,
                                        category,
                                        tags);

        this.photoRepository.saveAndFlush(photoEntity);
        this.categoryRepository.saveAndFlush(category);
        return "redirect:/";
    }

    @GetMapping("/photo/{id}")
    public String details(Model model, @PathVariable Integer id) {
        if (!this.photoRepository.exists(id)) {
            return "redirect:/";
        }
        if (!(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
            UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User entityUser = this.userRepository.findByEmail(principal.getUsername());

            model.addAttribute("user", entityUser);
        }
        List<Comment> comments = this.commentRepository.findAll();
        List<Comment> commentsByPhoto = new LinkedList<>();

        for (Comment comment : comments) {
            if (comment.getPhoto().getId() == id){
                commentsByPhoto.add(comment);
            }
        }
        Photo photo = this.photoRepository.findOne(id);
        model.addAttribute("photo", photo);
        model.addAttribute("view", "photo/details");
        model.addAttribute("comments", commentsByPhoto);
        return "base-layout";
    }

    @GetMapping("/photo/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String edit(@PathVariable Integer id, Model model) {
        if (!this.photoRepository.exists(id)) {
            return "redirect:/";
        }
        Photo photo = this.photoRepository.findOne(id);

        if (!isUserAuthorOrAdmin(photo)) {
            return "redirect:/photo/" + id;
        }

        List<Album> albums = this.albumRepository.findAll();
        List<Category> categories =this.categoryRepository.findAll();
        String tagString = photo.getTags().stream().map(Tag::getName).collect(Collectors.joining(", "));

        model.addAttribute("albums", albums);
        model.addAttribute("categories", categories);
        model.addAttribute("tags", tagString);
        model.addAttribute("photo", photo);
        model.addAttribute("view", "photo/edit");
        return "base-layout";

    }

    @PostMapping("/photo/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String editProcess(@PathVariable Integer id, PhotoBindingModel photoBindingModel) {
        if (StringUtils.isEmpty(photoBindingModel.getTitle()) || StringUtils.isEmpty(photoBindingModel.getContent())) {
            return "redirect:/photo/" + id;
        }
        if (!this.photoRepository.exists(id)) {
            return "redirect:/";
        }

        Photo photo = this.photoRepository.findOne(id);

        if (!isUserAuthorOrAdmin(photo)) {
            return "redirect:/photo/" + id;
        }

        Album album = this.albumRepository.findOne(photoBindingModel.getAlbumId());
        Category category =this.categoryRepository.findOne(photoBindingModel.getCategoryId());
        HashSet<Tag> tags = this.findTagsFromString(photoBindingModel.getTagString());

        photo.setAlbum(album);
        photo.setCategory(category);
        photo.setContent(photoBindingModel.getContent());
        photo.setTitle(photoBindingModel.getTitle());
        photo.setTags(tags);

        this.photoRepository.saveAndFlush(photo);
        return "redirect:/photo/" + id;
    }

    @GetMapping("/photo/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String delete(@PathVariable Integer id, Model model) {
        if (!this.photoRepository.exists(id)) {
            return "redirect:/";
        }
        Photo photo = this.photoRepository.findOne(id);


        if (!isUserAuthorOrAdmin(photo)) {
            return "redirect:/photo/" + id;
        }
        model.addAttribute("photo", photo);
        model.addAttribute("view", "photo/delete");
        return "base-layout";
    }

    @PostMapping("/photo/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String deleteProcess(@PathVariable Integer id, PhotoBindingModel photoBindingModel) {
        if (!this.photoRepository.exists(id)) {
            return "redirect:/";
        }
        Photo photo = this.photoRepository.findOne(id);

        if (!isUserAuthorOrAdmin(photo)) {
            return "redirect:/photo/" + id;
        }

        for (Comment comment : photo.getComments()){
            this.commentRepository.delete(comment);
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

    private HashSet<Tag> findTagsFromString(String tagString) {
        HashSet<Tag> tags = new HashSet<>();
        String[] tagNames = tagString.split(",\\s*");

        for(String tagName : tagNames){
            Tag currentTag = this.tagRepository.findByName(tagName);

            if(currentTag == null){
                currentTag = new Tag(tagName);
                this.tagRepository.saveAndFlush(currentTag);
            }

            tags.add(currentTag);
        }

        return tags;
    }
}
