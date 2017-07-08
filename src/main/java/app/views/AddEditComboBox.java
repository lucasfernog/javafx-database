package app.views;

import app.controllers.model.dialog.ModelDialogController;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.skins.JFXComboBoxListViewSkin;
import database.model.Model;

public class AddEditComboBox<T extends Model, C extends ModelDialogController<T>> extends JFXComboBox<T> {

    public void setDialogSupplier(AddEditComboBoxCell.DialogSupplier<T, C> dialogSupplier) {
        setupAddEditComboBox(dialogSupplier);
    }

    private void setupAddEditComboBox( AddEditComboBoxCell.DialogSupplier<T, C> dialogSupplier) {
        setCellFactory(list -> new AddEditComboBoxCell<>(this, dialogSupplier));
        setSkin(new JFXComboBoxListViewSkin<T>(this) {
            @Override
            protected boolean isHideOnClickEnabled() {
                return false;
            }
        });
    }
}
