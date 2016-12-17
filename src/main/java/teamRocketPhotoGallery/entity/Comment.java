package teamRocketPhotoGallery.entity;

import javax.persistence.*;
import javax.xml.crypto.Data;
import java.util.Date;

/**
 * Created by petar on 15/12/2016.
 */

@Entity
@Table(name = "comments")
public class Comment {

    private Integer id;

    private User author;

    private Photo photo;

    private String content;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne()
    @JoinColumn(nullable = false, name = "authorId")
    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    @Column(columnDefinition = "text", nullable = false)
    public String getContent() {
        return content;
    }


    public void setContent(String content) {
        this.content = content;
    }

    @ManyToOne()
    @JoinColumn(nullable = false, name = "photoId")
    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public Comment(){}

    public Comment(String content, User author, Photo photo){
        this.content = content;
        this.author = author;
        this.photo = photo;
    }
}
