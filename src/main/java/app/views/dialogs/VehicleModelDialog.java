package app.views.dialogs;

import app.controllers.model.dialog.VehicleModelDialogController;
import database.model.VehicleModel;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class VehicleModelDialog extends ModelDialog<VehicleModel, VehicleModelDialogController> {

    public VehicleModelDialog(StackPane dialogContainer, VehicleModel vehicleModel) throws IOException {
        this(dialogContainer, vehicleModel, true);
    }

    public VehicleModelDialog(StackPane dialogContainer, VehicleModel vehicleModel, boolean canSave) throws IOException {
        super(dialogContainer, "/fxml/vehicle_model_dialog.fxml", vehicleModel, canSave);
    }

}
