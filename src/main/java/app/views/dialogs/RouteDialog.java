package app.views.dialogs;

import app.controllers.model.dialog.RouteDialogController;
import database.model.Route;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class RouteDialog extends ModelDialog<Route, RouteDialogController> {

    public RouteDialog(StackPane dialogContainer, Route route) throws IOException {
        this(dialogContainer, route, true);
    }

    public RouteDialog(StackPane dialogContainer, Route route, boolean canSave) throws IOException {
        super(dialogContainer, "/fxml/route_dialog.fxml", route, canSave);
    }

}
