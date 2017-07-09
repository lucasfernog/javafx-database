package app.views.dialogs;

import app.controllers.model.dialog.BusStopDialogController;
import database.model.BusStop;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class BusStopDialog extends ModelDialog<BusStop, BusStopDialogController> {

    public BusStopDialog(StackPane dialogContainer, BusStop busStop) throws IOException {
        this(dialogContainer, busStop, true);
    }

    public BusStopDialog(StackPane dialogContainer, BusStop busStop, boolean canSave) throws IOException {
        super(dialogContainer, "/fxml/bus_stop_dialog.fxml", busStop, canSave);
    }

}
