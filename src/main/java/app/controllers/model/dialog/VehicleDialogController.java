package app.controllers.model.dialog;

import app.controllers.MainController;
import app.views.AddEditComboBox;
import app.views.dialogs.VehicleModelDialog;
import app.views.textfields.LicensePlateTextField;
import database.model.Vehicle;
import database.model.Model;
import database.model.VehicleModel;
import javafx.fxml.FXML;
import util.NodeUtils;

public class VehicleDialogController extends ModelDialogController<Vehicle> {

    @FXML
    private LicensePlateTextField mLicensePlate;
    @FXML
    private AddEditComboBox<VehicleModel, VehicleModelDialogController> mModelComboBox;

    @Override
    Model.SaveCallback saveModel() {
        if (mModel == null)
            mModel = new Vehicle();


        if (!(NodeUtils.validateRequiredTextFields(mLicensePlate) || NodeUtils.validateRequired(mModelComboBox)))
            return null;

        mModel.setLicensePlate(mLicensePlate.removeMask());
        mModel.setModelId(mModelComboBox.getSelectionModel().getSelectedItem().getPrimaryKey());

        return mModel.save();
    }

    @Override
    public void loadModel(boolean canSave) {
        Vehicle vehicle = getModel();

        mLicensePlate.setText(vehicle.getLicensePlate());
        NodeUtils.selectItemById(mModelComboBox, vehicle.getModelId());

        if (!canSave)
            NodeUtils.disableAll(mLicensePlate, mModelComboBox);
    }

    @Override
    public void initialize() {
        super.initialize();

        NodeUtils.setupRequiredTextFields(mLicensePlate);

        mModelComboBox.setDialogSupplier(item -> new VehicleModelDialog(MainController.getRoot(), item));
        mModelComboBox.setItems(VehicleModel.getAll());
        mModelComboBox.getItems().add(0, new VehicleModel());

        //TODO license plate mask
    }
}
