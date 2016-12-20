package teamRocketPhotoGallery.controller.admin;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import teamRocketPhotoGallery.bindingModel.CategoryBindingModel;
import teamRocketPhotoGallery.entity.Album;
import teamRocketPhotoGallery.entity.Category;

import teamRocketPhotoGallery.entity.Photo;
import teamRocketPhotoGallery.repository.AlbumRepository;
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
    @Autowired
    private AlbumRepository albumRepository;

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
    public String createProcess(CategoryBindingModel categoryBindingModel, RedirectAttributes redirectAttributes){

        if (StringUtils.isEmpty(categoryBindingModel.getName())){
            redirectAttributes.addFlashAttribute("error", "The form must not be empty");
            return "redirect:/admin/categories/create";
        }

        Category category = new Category(categoryBindingModel.getName());
        this.categoryRepository.saveAndFlush(category);

        return "redirect:/admin/categories/";
    }
    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable Integer id){
        if (!this.categoryRepository.exists(id)){
            return "redirect:/admin/categories/";
        }
         Category category = this.categoryRepository.findOne(id);

        model.addAttribute("category", category);
        model.addAttribute("view", "admin/category/edit");

         return "base-layout";
    }
    @PostMapping("/edit/{id}")
    public String editProcess(@PathVariable Integer id, CategoryBindingModel categoryBindingModel){
        if (!this.categoryRepository.exists(id)){
            return "redirect:/admin/categories/";
        }
        Category category = this.categoryRepository.findOne(id);

        category.setName(categoryBindingModel.getName());
        this.categoryRepository.saveAndFlush(category);

        return "redirect:/admin/categories/";
    }
    @GetMapping("/delete/{id}")
    public String delete(Model model,@PathVariable Integer id){
        if (!this.categoryRepository.exists(id)){
            return "redirect:/admin/categories/";
        }
        Category category = this.categoryRepository.findOne(id);

        model.addAttribute("category", category);
        model.addAttribute("view", "admin/category/delete");

        return "base-layout";
    }
    @PostMapping("/delete/{id}")
    public String deleteProcess(@PathVariable Integer id){
        if (!this.categoryRepository.exists(id)){
            return "redirect:/admin/categories/";
        }
        Category category = this.categoryRepository.findOne(id);

        for (Photo photo: category.getPhotos()) {
            this.photoRepository.delete(photo);
        }
        for (Album album: category.getAlbums()) {
            this.albumRepository.delete(album);
        }

        this.categoryRepository.delete(category);

        return "redirect:/admin/categories/";
    }

}
