package app.controllers.model.dialog;

import app.controllers.MainController;
import app.views.AddEditComboBox;
import app.views.dialogs.ManufacturerDialog;
import com.jfoenix.controls.JFXTextField;
import database.model.Manufacturer;
import database.model.Model;
import database.model.VehicleModel;
import javafx.fxml.FXML;
import util.NodeUtils;

public class VehicleModelDialogController extends ModelDialogController<VehicleModel> {

    @FXML
    private AddEditComboBox<Manufacturer, ManufacturerDialogController> mManufacturer;
    @FXML
    private JFXTextField mName;

    @Override
    Model.SaveCallback saveModel() {
        if (mModel == null)
            mModel = new VehicleModel();


        if (!(NodeUtils.validateRequiredTextFields(mName) && NodeUtils.validateRequired(mManufacturer)))
            return null;

        mModel.setName(mName.getText());
        mModel.setManufacturerId(mManufacturer.getSelectionModel().getSelectedItem().getPrimaryKey());

        return mModel.save();
    }

    @Override
    public void loadModel(boolean canSave) {
        VehicleModel vehicleModel = getModel();

        NodeUtils.selectItemById(mManufacturer, vehicleModel.getManufacturerId());
        mName.setText(vehicleModel.getName());

        if (!canSave)
            NodeUtils.disableAll(mManufacturer, mName);
    }

    @Override
    public void initialize() {
        super.initialize();

        mManufacturer.setDialogSupplier(item -> new ManufacturerDialog(MainController.getRoot(), item));
        mManufacturer.setItems(Manufacturer.getAll());
        mManufacturer.getItems().add(0, new Manufacturer());

        NodeUtils.setupRequiredTextFields(mName);
    }
}
