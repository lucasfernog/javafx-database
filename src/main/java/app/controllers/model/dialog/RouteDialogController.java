package app.controllers.model.dialog;

import app.controllers.MainController;
import app.views.AddEditComboBox;
import app.views.dialogs.RouteDialog;
import com.jfoenix.controls.JFXTextField;
import database.model.Model;
import database.model.Route;
import javafx.fxml.FXML;
import util.NodeUtils;

public class RouteDialogController extends ModelDialogController<Route> {

    @FXML
    private JFXTextField mDescription;
    @FXML
    private AddEditComboBox<Route, RouteDialogController> mReverseRoute;

    @Override
    Model.SaveCallback saveModel() {
        if (mModel == null)
            mModel = new Route();


        if (!NodeUtils.validateRequiredTextFields(mDescription))
            return null;

        mModel.setName(mDescription.getText());
        mModel.setReverseRouteId(mReverseRoute.getSelectionModel().getSelectedItem().getPrimaryKey());

        return mModel.save();
    }

    @Override
    public void loadModel(boolean canSave) {
        Route route = getModel();

        mDescription.setText(route.getName());
        int reverseRouteId = route.getReverseRouteId();
        if (reverseRouteId > 0)
            NodeUtils.selectItemById(mReverseRoute, reverseRouteId);

        if (!canSave)
            NodeUtils.disableAll(mDescription, mReverseRoute);
    }

    @Override
    public void initialize() {
        super.initialize();

        NodeUtils.setupRequiredTextFields(mDescription);

        mReverseRoute.setSuppliers(item -> new RouteDialog(MainController.getRoot(), item), Route::new);
        mReverseRoute.setItems(Route.getAll());
        mReverseRoute.getItems().add(0, new Route());
    }
}
