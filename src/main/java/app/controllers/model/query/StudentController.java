package app.controllers.model.query;

import app.views.dialogs.ModelDialog;
import app.views.dialogs.StudentDialog;
import com.jfoenix.controls.JFXTreeTableColumn;
import database.Database;
import database.model.Student;
import io.datafx.controller.ViewController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import util.NodeUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;

@ViewController(value = "/fxml/students.fxml")
public class StudentController extends ModelQueryController<Student> {

    @FXML
    private StackPane mRoot;

    @FXML
    private JFXTreeTableColumn<Student, Integer> mCodeColumn;

    @Override
    ObservableList<Student> getList() {
        ObservableList<Student> list = FXCollections.observableArrayList();

        Database.from(Student.class)
                .select("*")
                .execute(new Database.Callback<Student>() {
                    public void onSuccess(Student student) {
                        list.add(student);
                    }
                });

        return list;
    }

    @Override
    ModelDialog getModelDialog(Student student, boolean canSave) throws IOException {
        return new StudentDialog(mRoot, student, canSave);
    }

    @PostConstruct
    public void init() {
        super.init();

        NodeUtils.setupCellValueFactory(mCodeColumn, student -> student.primaryKeyProperty().asObject());
    }
}
