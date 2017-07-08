package app.controllers.model.dialog;

import app.controllers.MainController;
import app.views.AddEditComboBox;
import app.views.dialogs.VehicleModelDialog;
import com.jfoenix.controls.JFXTextField;
import database.model.Vehicle;
import database.model.Model;
import database.model.VehicleModel;
import javafx.fxml.FXML;
import util.NodeUtils;

public class VehicleDialogController extends ModelDialogController<Vehicle> {

    @FXML
    private JFXTextField mLicensePlate;
    @FXML
    private AddEditComboBox<VehicleModel, VehicleModelDialogController> mModelComboBox;

    @Override
    Model.SaveCallback saveModel() {
        if (mModel == null)
            mModel = new Vehicle();


        if (!(NodeUtils.validateRequiredTextFields(mLicensePlate) || NodeUtils.validateRequiredComboBoxes(mModelComboBox)))
            return null;

        mModel.setLicensePlate(mLicensePlate.getText());
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

        mModelComboBox.setSuppliers(item -> new VehicleModelDialog(MainController.getRoot(), item), VehicleModel::new);
        mModelComboBox.setItems(VehicleModel.getAll());
        mModelComboBox.getItems().add(0, new VehicleModel());

        //TODO license plate mask
    }
}
