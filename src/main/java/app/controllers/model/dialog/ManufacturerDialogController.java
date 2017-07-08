package app.controllers.model.dialog;

import com.jfoenix.controls.JFXTextField;
import database.model.Manufacturer;
import database.model.Model;
import javafx.fxml.FXML;
import util.NodeUtils;

public class ManufacturerDialogController extends ModelDialogController<Manufacturer> {

    @FXML
    private JFXTextField mName;

    @Override
    Model.SaveCallback saveModel() {
        if (mModel == null)
            mModel = new Manufacturer();


        if (!NodeUtils.validateRequiredTextFields(mName))
            return null;

        mModel.setName(mName.getText());

        return mModel.save();
    }

    @Override
    public void loadModel(boolean canSave) {
        Manufacturer manufacturer = getModel();

        mName.setText(manufacturer.getName());

        if (!canSave)
            NodeUtils.disableAll(mName);
    }

    @Override
    public void initialize() {
        super.initialize();

        NodeUtils.setupRequiredTextFields(mName);
    }
}
