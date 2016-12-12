package teamRocketPhotoGallery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;
import teamRocketPhotoGallery.bindingModel.AlbumBindingModel;
import teamRocketPhotoGallery.entity.Album;
import teamRocketPhotoGallery.entity.Photo;
import teamRocketPhotoGallery.repository.AlbumRepository;
import teamRocketPhotoGallery.repository.PhotoRepository;
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

        return "base-layout";
    }

    @PostMapping("/create")
    public String createProcess(AlbumBindingModel albumBindingModel){
        if(StringUtils.isEmpty(albumBindingModel.getName())){
            return "redirect:/albums/create";
        }

        Album album = new Album(albumBindingModel.getName());

        this.albumRepository.saveAndFlush(album);

        return "redirect:/albums/";
    }

    @GetMapping("/delete/{id}")
    public String delete(Model model, @PathVariable Integer id){
        if(!this.albumRepository.exists(id)){
            return "redirect:/albums/";
        }

        Album album = this.albumRepository.findOne(id);

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
}
