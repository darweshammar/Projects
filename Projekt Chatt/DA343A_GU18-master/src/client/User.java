package client;

import javax.swing.*;
import java.io.Serializable;

public class User implements Serializable {

    private String name;
    private ImageIcon profilePic;

    public User(String name, ImageIcon profilePic) {
        this.name = name;
        this.profilePic = profilePic;
    }





    public String getName() {
        return name;
    }

    public ImageIcon getProfilePic() {
        return profilePic;
    }
}
