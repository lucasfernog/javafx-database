package app.views.dialogs;

import app.controllers.model.dialog.StudentDialogController;
import app.controllers.model.dialog.UserDialogController;
import database.model.Student;
import database.model.User;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class StudentDialog extends ModelDialog<Student, StudentDialogController> {

    public StudentDialog(StackPane dialogContainer, Student student) throws IOException {
        this(dialogContainer, student, true);
    }

    public StudentDialog(StackPane dialogContainer, Student student, boolean canSave) throws IOException {
        super(dialogContainer, "/fxml/student_dialog.fxml", student, canSave);
    }

}
