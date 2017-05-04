package app.views.dialogs;

import app.controllers.BusDialogController;
import database.model.Bus;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class BusDialog extends ModelDialog<Bus, BusDialogController> {

    public BusDialog(StackPane dialogContainer, Bus bus) throws IOException {
        this(dialogContainer, bus, true);
    }

    public BusDialog(StackPane dialogContainer, Bus bus, boolean canSave) throws IOException {
        super(dialogContainer, "/fxml/bus_dialog.fxml", bus, canSave);
    }

}
