package app.views.dialogs;

import app.controllers.model.dialog.DriverDialogController;
import app.controllers.model.dialog.ManufacturerDialogController;
import database.model.Driver;
import database.model.Manufacturer;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class DriverDialog extends ModelDialog<Driver, DriverDialogController> {

    public DriverDialog(StackPane dialogContainer, Driver driver) throws IOException {
        this(dialogContainer, driver, true);
    }

    public DriverDialog(StackPane dialogContainer, Driver driver, boolean canSave) throws IOException {
        super(dialogContainer, "/fxml/driver_dialog.fxml", driver, canSave);
    }

}
