package app.controllers.model.dialog;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import database.model.Manufacturer;
import database.model.Vehicle;
import database.model.Model;
import javafx.fxml.FXML;
import util.NodeUtils;

public class VehicleDialogController extends ModelDialogController<Vehicle> {

    @FXML
    private JFXTextField mLicensePlate;
    @FXML
    private JFXComboBox<Manufacturer> mManufacturer;
    @FXML
    private JFXTextField mModelTextField;

    @Override
    Model.SaveCallback saveModel() {
        if (mModel == null)
            mModel = new Vehicle();


        if (!NodeUtils.validateRequiredTextFields(mLicensePlate, mModelTextField) || !NodeUtils.validateRequiredComboBoxes(mManufacturer))
            return null;

        mModel.setManufacturerId(mManufacturer.getSelectionModel().getSelectedItem().getPrimaryKey());
        mModel.setLicensePlate(mLicensePlate.getText());
        mModel.setModel(mModelTextField.getText());

        return mModel.save();
    }

    @Override
    public void loadModel(boolean canSave) {
        Vehicle vehicle = getModel();

        mLicensePlate.setText(vehicle.getLicensePlate());
        mManufacturer.getSelectionModel().select(vehicle.getManufacturerId()); //TODO

        if (!canSave)
            NodeUtils.disableAll(mLicensePlate, mManufacturer);
    }

    @Override
    public void initialize() {
        super.initialize();

        NodeUtils.setupRequiredTextFields(mModelTextField, mLicensePlate);
        mManufacturer.setItems(Manufacturer.getAll());
        //TODO license plate mask
    }
}
