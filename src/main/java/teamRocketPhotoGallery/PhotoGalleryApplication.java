package teamRocketPhotoGallery;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import teamRocketPhotoGallery.storage.StorageService;

@SpringBootApplication
public class PhotoGalleryApplication {

    public static void main(String[] args) {
        SpringApplication.run(PhotoGalleryApplication.class, args);
    }
    @Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> {
            storageService.deleteAll();
            storageService.init();
        };
    }
}
