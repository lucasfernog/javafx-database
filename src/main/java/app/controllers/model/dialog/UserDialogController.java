package app.controllers.model.dialog;

import com.jfoenix.controls.JFXTextField;
import database.model.Model;
import database.model.User;
import javafx.fxml.FXML;
import util.NodeUtils;

public class UserDialogController extends ModelDialogController<User> {

    @FXML
    private JFXTextField mCPF;

    @FXML
    private JFXTextField mName;

    @FXML
    private JFXTextField mBalance;

    @Override
    Model.SaveCallback saveModel() {
        if (mModel == null)
            mModel = new User();


        if (!NodeUtils.validateRequiredTextFields(mCPF, mName, mBalance))
            return null;

        mModel.setCPF(Long.parseLong(mCPF.getText().replaceAll("[/.-]", "")));
        mModel.setName(mName.getText());
        mModel.setBalance(Float.parseFloat(mBalance.getText()));

        return mModel.save();
    }

    @Override
    public void loadModel(boolean canSave) {
        User user = getModel();

        mCPF.setText(String.valueOf(user.getCPF()));
        mName.setText(user.getName());
        mBalance.setText(String.valueOf(user.getBalance()));

        if (!canSave)
            NodeUtils.disableAll(mCPF, mName, mBalance);
    }

    @Override
    public void initialize() {
        super.initialize();

        NodeUtils.setupRequiredTextFields(mCPF, mName, mBalance);
    }
}
