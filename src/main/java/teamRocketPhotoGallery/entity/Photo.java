package teamRocketPhotoGallery.entity;

import javax.persistence.*;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "photos")
public class Photo {
    private Integer id;

    private String title;

    private String content;

    private User author;

    private Category category;

    private Album album;

    private Set<Tag> tags;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(nullable = false)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(columnDefinition = "text", nullable = false)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    @ManyToOne
    @JoinColumn(nullable = false, name = "albumId")
    public Album getAlbum(){ return album; }

    public void setAlbum(Album album){ this.album = album; }

    @ManyToMany()
    @JoinColumn(table = "photos_tags")
    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Photo(String title, String content, User author, Album album, Category category, HashSet<Tag> tags) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.album = album ;
        this.category = category;
        this.tags = tags;
    }

    public Photo() {
    }

    private Set<Comment> comments;

    @OneToMany(mappedBy = "author")
    public Set<Comment> getComments() {return comments;}

    public void setComments(Set<Comment> comments){this.comments = comments;}

}
