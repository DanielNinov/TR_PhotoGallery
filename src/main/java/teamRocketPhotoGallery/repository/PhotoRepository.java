package teamRocketPhotoGallery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import teamRocketPhotoGallery.entity.Photo;

public interface PhotoRepository extends JpaRepository<Photo, Integer> {
}
