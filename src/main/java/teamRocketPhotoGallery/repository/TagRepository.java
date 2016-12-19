package teamRocketPhotoGallery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import teamRocketPhotoGallery.entity.Tag;

/**
 * Created by Daniel on 19-Dec-16.
 */
public interface TagRepository extends JpaRepository<Tag, Integer> {
    Tag findByName(String name);
}
