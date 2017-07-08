package app.views.dialogs;

import app.controllers.model.dialog.ManufacturerDialogController;
import database.model.Manufacturer;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class ManufacturerDialog extends ModelDialog<Manufacturer, ManufacturerDialogController> {

    public ManufacturerDialog(StackPane dialogContainer, Manufacturer manufacturer) throws IOException {
        this(dialogContainer, manufacturer, true);
    }

    public ManufacturerDialog(StackPane dialogContainer, Manufacturer manufacturer, boolean canSave) throws IOException {
        super(dialogContainer, "/fxml/manufacturer_dialog.fxml", manufacturer, canSave);
    }

}
