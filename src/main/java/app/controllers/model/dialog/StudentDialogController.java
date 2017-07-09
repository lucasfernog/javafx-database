package app.controllers.model.dialog;

import app.controllers.MainController;
import app.views.AddEditComboBox;
import app.views.dialogs.UserDialog;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import database.model.Model;
import database.model.Student;
import database.model.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import util.NodeUtils;

import java.time.LocalDate;

public class StudentDialogController extends ModelDialogController<Student> {

    @FXML
    private JFXDatePicker mDueDate;

    @FXML
    private JFXTextField mBalance;

    @FXML
    private AddEditComboBox<User, UserDialogController> mUser;

    @Override
    Model.SaveCallback saveModel() {
        if (mModel == null)
            mModel = new Student();


        if (!(NodeUtils.validateRequiredTextFields(mBalance) && NodeUtils.validateRequired(mUser, mDueDate)))
            return null;

        mModel.setUserId(mUser.getSelectionModel().getSelectedItem().getPrimaryKey());
        mModel.setDueDate(mDueDate.getValue().toString());
        mModel.setBalance(Float.parseFloat(mBalance.getText()));

        return mModel.save();
    }

    @Override
    public void loadModel(boolean canSave) {
        Student student = getModel();

        NodeUtils.selectItemById(mUser, student.getUserId());
        mDueDate.setValue(LocalDate.parse(mModel.getDueDate()));
        mBalance.setText(String.valueOf(student.getBalance()));

        if (!canSave)
            NodeUtils.disableAll(mUser, mDueDate, mBalance);
    }

    @Override
    public void initialize() {
        super.initialize();

        NodeUtils.setupRequiredTextFields(mBalance);

        Platform.runLater(() -> {
            Student student = getModel();
            mUser.setItems(User.getNonUsers(student == null ? null : student.getUserId()));
            mUser.getItems().add(0, new User());
        });

        mUser.setDialogSupplier(item -> new UserDialog(MainController.getRoot(), item));


    }
}
