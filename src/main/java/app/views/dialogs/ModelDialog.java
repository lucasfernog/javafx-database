package app.views.dialogs;

import app.controllers.model.dialog.ModelDialogController;
import com.jfoenix.controls.JFXDialog;
import database.model.Model;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public abstract class ModelDialog<M extends Model, C extends ModelDialogController<M>> extends JFXDialog {

    public interface OnSaveListener<M> {
        void onSave(M item);
    }

    private C controller;

    ModelDialog(StackPane dialogContainer, String xmlPath) throws IOException {
        this(dialogContainer, xmlPath, null);
    }

    ModelDialog(StackPane dialogContainer, String xmlPath, M model) throws IOException {
        this(dialogContainer, xmlPath, model, true);
    }

    ModelDialog(StackPane dialogContainer, String xmlPath, M model, boolean canSave) throws IOException {
        super(dialogContainer,
                null,
                DialogTransition.CENTER);

        //carrega XML
        FXMLLoader loader = new FXMLLoader(getClass().getResource(xmlPath));
        Parent root = loader.load();

        //define layout como conteúdo do dialog
        setContent((Region) root);

        controller = loader.getController();
        controller.setCloseDialogCallback(this::close);
        //injeta a model no controller
        if (model != null) {
            controller.setModel(model, canSave);
        }
    }

    public void setOnSaveListener(OnSaveListener<M> onSaveListener) {
        controller.setOnSaveListener(onSaveListener);
    }
}
