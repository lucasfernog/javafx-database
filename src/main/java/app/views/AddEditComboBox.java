package app.views;

import app.controllers.model.dialog.ModelDialogController;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.skins.JFXComboBoxListViewSkin;
import database.model.Model;

import java.util.function.Supplier;

public class AddEditComboBox<T extends Model, C extends ModelDialogController<T>> extends JFXComboBox<T> {

    private AddEditComboBoxCell.DialogSupplier<T, C> mDialogSupplier;
    private Supplier<T> mModelSupplier;

    public void setSuppliers(AddEditComboBoxCell.DialogSupplier<T, C> dialogSupplier, Supplier<T> modelSupplier) {
        mDialogSupplier = dialogSupplier;
        mModelSupplier = modelSupplier;
        setupAddEditComboBox();
    }

    private void setupAddEditComboBox() {
        setCellFactory(list -> new AddEditComboBoxCell<>(this, mDialogSupplier, mModelSupplier));
        setSkin(new JFXComboBoxListViewSkin<T>(this) {
            @Override
            protected boolean isHideOnClickEnabled() {
                return false;
            }
        });
    }
}
