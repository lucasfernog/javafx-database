package app.views;

import app.controllers.model.dialog.ModelDialogController;
import app.views.dialogs.ModelDialog;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXRippler;
import database.model.Model;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import util.NodeUtils;

import java.io.IOException;

public class AddEditComboBoxCell<T extends Model, C extends ModelDialogController<T>> extends JFXListCell<T> {

    public interface DialogSupplier<T extends Model, C extends ModelDialogController<T>> {
        ModelDialog<T, C> newDialog(T item) throws IOException;
    }

    private JFXComboBox<T> mComboBox;
    private DialogSupplier<T, C> mDialogSupplier;

    public AddEditComboBoxCell(JFXComboBox<T> comboBox, DialogSupplier<T, C> dialogSupplier) {
        mComboBox = comboBox;
        mDialogSupplier = dialogSupplier;
    }

    @Override
    public void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);

        boolean addCell = item == null || item.toString().isEmpty();

        ImageView imageView = new ImageView(new Image(addCell ? "images/ic_add.png" : "images/ic_edit.png"));
        NodeUtils.tintDark(imageView);
        JFXRippler rippler = new JFXRippler(imageView);
        HBox graphicContainer = new HBox(rippler);
        setGraphic(graphicContainer);

        setText(cellContent == null ? "" : ((Label) cellContent).getText());

        setOnMouseClicked(e -> {
            if (e.getTarget() == imageView)
                try {
                    ModelDialog<T, C> dialog = mDialogSupplier.newDialog(addCell ? null : item);
                    dialog.setOnSaveListener((model) -> {
                        if (addCell) {
                            mComboBox.getItems().add(model);
                            Platform.runLater(() -> mComboBox.getSelectionModel().select(model));
                        } else {
                            mComboBox.getItems().set(mComboBox.getItems().indexOf(item), model);
                            //TODO update text
                        }
                    });
                    dialog.show();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            mComboBox.hide();
        });
    }
}
