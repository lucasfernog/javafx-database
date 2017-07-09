package app.controllers.model.query;

import app.views.dialogs.ModelDialog;
import app.views.dialogs.UserDialog;
import app.views.dialogs.VehicleDialog;
import com.jfoenix.controls.JFXTreeTableColumn;
import database.Database;
import database.model.User;
import database.model.Vehicle;
import io.datafx.controller.ViewController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import util.NodeUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;

@ViewController(value = "/fxml/users.fxml")
public class UserController extends ModelQueryController<User> {

    @FXML
    private StackPane mRoot;

    @FXML
    private JFXTreeTableColumn<User, Integer> mCodeColumn;

    @Override
    ObservableList<User> getList() {
        ObservableList<User> list = FXCollections.observableArrayList();

        Database.from(User.class)
                .select("*")
                .execute(new Database.Callback<User>() {
                    public void onSuccess(User user) {
                        list.add(user);
                    }
                });

        return list;
    }

    @Override
    ModelDialog getModelDialog(User user, boolean canSave) throws IOException {
        return new UserDialog(mRoot, user, canSave);
    }

    @PostConstruct
    public void init() {
        super.init();

        NodeUtils.setupCellValueFactory(mCodeColumn, user -> user.primaryKeyProperty().asObject());
    }
}
