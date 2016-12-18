package teamRocketPhotoGallery.bindingModel;


import javax.validation.constraints.NotNull;

/**
 * Created by Daniel on 12-Dec-16.
 */
public class AlbumBindingModel {
    @NotNull
    private String name;

    @NotNull
    private Integer categoryId;

    public String getName() { return name; }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
}
