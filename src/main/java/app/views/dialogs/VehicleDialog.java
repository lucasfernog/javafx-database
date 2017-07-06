package app.views.dialogs;

import app.controllers.model.dialog.VehicleDialogController;
import database.model.Vehicle;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class VehicleDialog extends ModelDialog<Vehicle, VehicleDialogController> {

    public VehicleDialog(StackPane dialogContainer, Vehicle vehicle) throws IOException {
        this(dialogContainer, vehicle, true);
    }

    public VehicleDialog(StackPane dialogContainer, Vehicle vehicle, boolean canSave) throws IOException {
        super(dialogContainer, "/fxml/vehicle_dialog.fxml", vehicle, canSave);
    }

}
