package teamRocketPhotoGallery.entity;

import javax.persistence.*;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
@Table(name = "photos")
public class Photo {
    private Integer id;

    private String title;

    private String content;

    private User author;

    private Album album;

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
    @JoinColumn(nullable = false, name = "albumId")
    public Album getAlbum(){ return album; }

    public void setAlbum(Album album){ this.album = album; }

    @Transient
    public String getPreview() {
        Integer limit = 300;
        Integer length = this.getContent().length();
        if (length > limit) {
            return this.getContent().substring(0, limit) + "...";
        } else {
            return this.getContent();
        }
    }

    public Photo(String title, String content, User author, Album album) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.album = album;
    }

    public Photo() {
    }
}
