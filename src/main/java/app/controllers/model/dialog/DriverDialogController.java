package app.controllers.model.dialog;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import database.model.Driver;
import database.model.Model;
import javafx.fxml.FXML;
import util.NodeUtils;

import java.time.LocalDate;

public class DriverDialogController extends ModelDialogController<Driver> {

    @FXML
    private JFXTextField mCPF;
    @FXML
    private JFXTextField mCTPS;
    @FXML
    private JFXTextField mName;
    @FXML
    private JFXDatePicker mBirthDate;
    @FXML
    private JFXDatePicker mHiringDate;
    @FXML
    private JFXTextField mSalary;

    @Override
    Model.SaveCallback saveModel() {
        if (mModel == null)
            mModel = new Driver();


        if (!(NodeUtils.validateRequiredTextFields(mCPF, mCTPS, mName, mSalary) && NodeUtils.validateRequiredDatePickers(mBirthDate, mHiringDate)))
            return null;

        mModel.setCPF(Long.parseLong(mCPF.getText().replaceAll("[/.-]", "")));
        mModel.setCTPS(Long.parseLong(mCTPS.getText()));
        mModel.setName(mName.getText());
        mModel.setBirthDate(mBirthDate.getValue().toString());
        mModel.setHiringDate(mHiringDate.getValue().toString());
        mModel.setSalary(Float.parseFloat(mSalary.getText().replace(",", ".")));

        return mModel.save();
    }

    @Override
    public void loadModel(boolean canSave) {
        Driver driver = getModel();

        mCPF.setText(String.valueOf(mModel.getCPF()));
        mCTPS.setText(String.valueOf(mModel.getCTPS()));
        mName.setText(driver.getName());
        mBirthDate.setValue(LocalDate.parse(mModel.getBirthDate()));
        mHiringDate.setValue(LocalDate.parse(mModel.getHiringDate()));
        mSalary.setText(String.valueOf(mModel.getSalary()));

        if (!canSave)
            NodeUtils.disableAll(mCPF, mCTPS, mName, mBirthDate, mHiringDate, mSalary);
    }

    @Override
    public void initialize() {
        super.initialize();

        NodeUtils.setupRequiredTextFields(mCPF, mCTPS, mName, mSalary);
    }
}
