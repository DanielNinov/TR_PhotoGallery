package teamRocketPhotoGallery.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Daniel on 19-Dec-16.
 */
@Entity
@Table(name = "tags")
public class Tag {
    private Integer id;

    private String name;

    private Set<Photo> photos;

    public Tag() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(unique = true, nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToMany(mappedBy = "tags")
    public Set<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(Set<Photo> photos) {
        this.photos = photos;
    }

    public Tag(String name) {
        this.name = name;
        this.photos = new HashSet<>();
    }
}
