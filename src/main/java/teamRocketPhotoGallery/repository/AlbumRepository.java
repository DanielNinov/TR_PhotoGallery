package teamRocketPhotoGallery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import teamRocketPhotoGallery.entity.Album;

/**
 * Created by Daniel on 12-Dec-16.
 */
public interface AlbumRepository extends JpaRepository<Album, Integer> {
}
