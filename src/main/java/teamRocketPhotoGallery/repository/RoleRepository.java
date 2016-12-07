package teamRocketPhotoGallery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import teamRocketPhotoGallery.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(String name);
}