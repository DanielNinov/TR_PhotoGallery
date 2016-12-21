package teamRocketPhotoGallery.bindingModel;

import javax.validation.constraints.NotNull;

public class PhotoBindingModel {
    @NotNull
    private String title;
    @NotNull
    private String content;

    private Integer albumId;

    private Integer categoryId;

    private String tagString;

    public String getTagString() {
        return tagString;
    }

    public void setTagString(String tagString) { this.tagString = tagString; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getAlbumId() { return albumId; }

    public void setAlbumId(Integer albumId) { this.albumId = albumId; }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
}
