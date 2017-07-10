package app.views.dialogs;

import app.controllers.model.dialog.AlertDialogController;
import com.jfoenix.controls.JFXDialog;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class AlertDialog extends JFXDialog {

    public AlertDialog(StackPane dialogContainer, String title, String content) {
        super(dialogContainer, null,
                DialogTransition.CENTER);
        try {
            //carrega XML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/alert_dialog.fxml"));
            Parent root = loader.load();

            //define layout como conteúdo do dialog
            setContent((Region) root);

            AlertDialogController controller = loader.getController();
            controller.setDialog(this);
            controller.setTitle(title);
            controller.setContent(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
