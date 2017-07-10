package app.controllers.model.dialog;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AlertDialogController {

    @FXML
    private Label mTitle;
    @FXML
    private Label mContent;
    @FXML
    private JFXButton mOkButton;

    private JFXDialog mDialog;

    public void initialize() {
        mOkButton.setOnAction(e -> mDialog.close());
    }

    public void setDialog(JFXDialog dialog) {
        mDialog = dialog;
    }

    public void setTitle(String title) {
        mTitle.setText(title);
    }

    public void setContent(String content) {
        mContent.setText(content);
    }

}
