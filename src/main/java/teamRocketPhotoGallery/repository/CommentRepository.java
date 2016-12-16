package teamRocketPhotoGallery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import teamRocketPhotoGallery.entity.Comment;

/**
 * Created by petar on 15/12/2016.
 */

public interface CommentRepository extends JpaRepository<Comment, Integer>{

}
