package teamRocketPhotoGallery.bindingModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fl3x1324 on 29.11.16.
 */
public class UserEditBindingModel extends UserBindingModel {
    private List<Integer> roles;

    public UserEditBindingModel() {
        this.roles = new ArrayList<>();
    }

    public List<Integer> getRoles() {

        return roles;
    }

    public void setRoles(List<Integer> roles) {
        this.roles = roles;
    }
}
