package teamRocketPhotoGallery.bindingModel;

import javax.validation.constraints.NotNull;

/**
 * Created by petar on 15/12/2016.
 */
public class CommentBindingModel {

    @NotNull
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
