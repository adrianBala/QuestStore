package server;

import model.User;
import org.jtwig.JtwigModel;

public class MentorPage {

    public static void setModel(User user, JtwigModel model){
        model.with("name", user.getName());
        model.with("email", user.getEmail());
    }

}
