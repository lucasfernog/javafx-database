package app.controllers.model.query;

import app.controllers.MainController;
import app.views.dialogs.ModelDialog;
import app.views.dialogs.UserDialog;
import app.views.textfields.FloatTextField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import database.Database;
import database.QueryBuilder;
import database.model.User;
import io.datafx.controller.ViewController;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import util.NodeUtils;
import util.Utils;

import javax.annotation.PostConstruct;
import java.io.IOException;

@ViewController(value = "/fxml/users.fxml")
public class UserController extends ModelQueryController<User> {

    @FXML
    private StackPane mRoot;

    @FXML
    private JFXTextField mSearchCode;
    @FXML
    private JFXTextField mSearchName;
    @FXML
    private FloatTextField mSearchBalance;

    @FXML
    private JFXTreeTableColumn<User, Number> mCodeColumn;
    @FXML
    private JFXTreeTableColumn<User, String> mNameColumn;
    @FXML
    private JFXTreeTableColumn<User, Number> mBalanceColumn;

    @Override
    QueryBuilder<User> getSearchQuery() {
        QueryBuilder<User> query = Database.from(User.class)
                .select("*");

        String searchCode = mSearchCode.getText();
        if (!Utils.isEmpty(searchCode))
            query.where("codigo", "=", Integer.valueOf(searchCode));

        String searchName = mSearchName.getText();
        if (!Utils.isEmpty(searchName))
            query.where("nome", "ILIKE", "%" + searchName + "%");

        String searchBalance = mSearchBalance.getText();
        if (!Utils.isEmpty(searchBalance))
            query.where("saldo", "=", Float.valueOf(searchBalance));

        return query;
    }

    @Override
    void clearQuery() {
        mSearchCode.setText("");
        mSearchName.setText("");
        mSearchBalance.setText("");
    }

    @Override
    ModelDialog getModelDialog(User user, boolean canSave) throws IOException {
        return new UserDialog(mRoot, user, canSave);
    }

    @PostConstruct
    public void init() {
        super.init();

        MainController.setTitle("Usuários");

        NodeUtils.setupCellValueFactory(mCodeColumn, User::primaryKeyProperty);
        NodeUtils.setupCellValueFactory(mNameColumn, User::nameProperty);
        NodeUtils.setupCellValueFactory(mBalanceColumn, User::balanceProperty);
    }
}
