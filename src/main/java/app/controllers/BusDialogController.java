package app.controllers;

import com.jfoenix.controls.JFXTextField;
import database.model.Bus;
import database.model.Model;
import javafx.fxml.FXML;
import util.Utils;

public class BusDialogController extends ModelDialogController<Bus> {

    @FXML
    private JFXTextField mCode;

    @Override
    Model.SaveCallback saveModel() {
        if (mModel == null)
            mModel = new Bus();

        //TODO: feedback de erro no textfield
        String code = mCode.getText();
        if (code.equals(""))
            return null;

        mModel.setCode(Integer.valueOf(code));

        return mModel.save();
    }

    @Override
    public void loadModel(boolean canSave) {
        Bus bus = getModel();
        mCode.setText(String.valueOf(bus.getCode()));

        if (!canSave) {
            mCode.setDisable(true);
        }
    }

    @Override
    public void initialize() {
        super.initialize();
        Utils.numericTextInputControl(mCode);
    }
}
