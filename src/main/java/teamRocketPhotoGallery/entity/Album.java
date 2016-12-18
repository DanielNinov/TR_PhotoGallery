package teamRocketPhotoGallery.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Daniel on 12-Dec-16.
 */

// Not sure if it would work when the upload functionality is finished, probably needs to be edited.
// (Currently works with the URL upload)

@Entity
@Table(name = "albums")
public class Album {
    private Integer id;

    private String name;

    private Set<Photo> photos;

    private User author;

    private Category category;

    public Album() {
    }

    public Album(String name, User author, Category category) {
        this.name = name;
        this.author = author;
        this.category = category;
        this.photos = new HashSet<>();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() { return id; }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(nullable = false, unique = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "album")
    public Set<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(Set<Photo> photos) {
        this.photos = photos;
    }

    @ManyToOne
    @JoinColumn(name = "authorId")
    public User getAuthor() { return author; }

    public void setAuthor(User author) {
        this.author = author;
    }

    @ManyToOne()
    @JoinColumn(nullable = false, name = "categoryId")
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
