package app.controllers.model.query;

import app.views.dialogs.ModelDialog;
import app.views.dialogs.StudentDialog;
import app.views.textfields.FloatTextField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import database.Database;
import database.QueryBuilder;
import database.model.Student;
import io.datafx.controller.ViewController;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import util.NodeUtils;
import util.Utils;

import javax.annotation.PostConstruct;
import java.io.IOException;

@ViewController(value = "/fxml/students.fxml")
public class StudentController extends ModelQueryController<Student> {

    @FXML
    private StackPane mRoot;

    @FXML
    private JFXTextField mSearchCode;
    @FXML
    private JFXTextField mSearchName;
    @FXML
    private FloatTextField mSearchBalance;

    @FXML
    private JFXTreeTableColumn<Student, Number> mCodeColumn;
    @FXML
    private JFXTreeTableColumn<Student, String> mNameColumn;
    @FXML
    private JFXTreeTableColumn<Student, Number> mBalanceColumn;

    @Override
    QueryBuilder<Student> getSearchQuery() {
        QueryBuilder<Student> query = Database.from(Student.class)
                .select("estudantes.*", "nome")
                .innerJoin("usuarios", "usuarios.codigo", "=", "usuario");

        String searchCode = mSearchCode.getText();
        if (!Utils.isEmpty(searchCode))
            query.where("usuario", "=", Integer.valueOf(searchCode));

        String searchName = mSearchName.getText();
        if (!Utils.isEmpty(searchName))
            query.where("nome", "ILIKE", "%" + searchName + "%");

        String searchBalance = mSearchBalance.getText();
        if (!Utils.isEmpty(searchBalance))
            query.where("estudantes.saldo", "=", Float.valueOf(searchBalance));

        return query;
    }

    @Override
    void clearQuery() {
        mSearchCode.setText("");
        mSearchName.setText("");
        mSearchBalance.setText("");
    }

    @Override
    ModelDialog getModelDialog(Student student, boolean canSave) throws IOException {
        return new StudentDialog(mRoot, student, canSave);
    }

    @PostConstruct
    public void init() {
        super.init();

        NodeUtils.setupCellValueFactory(mCodeColumn, Student::primaryKeyProperty);
        NodeUtils.setupCellValueFactory(mNameColumn, student -> student.getUser().nameProperty());
        NodeUtils.setupCellValueFactory(mBalanceColumn, Student::balanceProperty);
    }
}
