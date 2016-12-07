package teamRocketPhotoGallery.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import teamRocketPhotoGallery.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
}