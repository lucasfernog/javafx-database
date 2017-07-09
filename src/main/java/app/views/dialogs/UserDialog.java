package app.views.dialogs;

import app.controllers.model.dialog.UserDialogController;
import database.model.User;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class UserDialog extends ModelDialog<User, UserDialogController> {

    public UserDialog(StackPane dialogContainer, User user) throws IOException {
        this(dialogContainer, user, true);
    }

    public UserDialog(StackPane dialogContainer, User user, boolean canSave) throws IOException {
        super(dialogContainer, "/fxml/user_dialog.fxml", user, canSave);
    }

}
