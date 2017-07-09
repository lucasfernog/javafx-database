package app.controllers.model.dialog;

import com.jfoenix.controls.JFXTextField;
import database.model.BusStop;
import database.model.Model;
import javafx.fxml.FXML;
import util.NodeUtils;

public class BusStopDialogController extends ModelDialogController<BusStop> {

    @FXML
    private JFXTextField mCEP;

    @FXML
    private JFXTextField mStreet;

    @FXML
    private JFXTextField mNumber;

    @Override
    Model.SaveCallback saveModel() {
        if (mModel == null)
            mModel = new BusStop();


        if (!NodeUtils.validateRequiredTextFields(mCEP, mStreet, mNumber))
            return null;

        mModel.setCEP(Integer.parseInt(mCEP.getText()));
        mModel.setStreet(mStreet.getText());
        mModel.setNumber(Integer.parseInt(mNumber.getText()));

        return mModel.save();
    }

    @Override
    public void loadModel(boolean canSave) {
        BusStop busStop = getModel();

        mCEP.setText(String.valueOf(busStop.getCEP()));
        mStreet.setText(busStop.getStreet());
        mNumber.setText(String.valueOf(busStop.getNumber()));

        if (!canSave)
            NodeUtils.disableAll(mCEP, mStreet, mNumber);
    }

    @Override
    public void initialize() {
        super.initialize();

        NodeUtils.setupRequiredTextFields(mCEP, mStreet, mNumber);
    }
}
