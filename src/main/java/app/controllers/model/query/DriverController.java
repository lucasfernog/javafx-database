package app.controllers.model.query;

import app.views.dialogs.DriverDialog;
import app.views.dialogs.ModelDialog;
import com.jfoenix.controls.JFXTreeTableColumn;
import database.Database;
import database.model.Driver;
import io.datafx.controller.ViewController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import util.NodeUtils;
import util.Utils;

import javax.annotation.PostConstruct;
import java.io.IOException;

@ViewController(value = "/fxml/drivers.fxml")
public class DriverController extends ModelQueryController<Driver> {

    @FXML
    private StackPane mRoot;

    @FXML
    private JFXTreeTableColumn<Driver, Integer> mCodeColumn;
    @FXML
    private JFXTreeTableColumn<Driver, String> mNameColumn;
    @FXML
    private JFXTreeTableColumn<Driver, Float> mSalaryColumn;
    @FXML
    private JFXTreeTableColumn<Driver, String> mHiringDateColumn;

    @Override
    ObservableList<Driver> getList() {
        ObservableList<Driver> driverList = FXCollections.observableArrayList();

        Database.from(Driver.class)
                .select("*")
                .execute(new Database.Callback<Driver>() {
                    public void onSuccess(Driver driver) {
                        driverList.add(driver);
                    }
                });

        return driverList;
    }

    @Override
    ModelDialog getModelDialog(Driver driver, boolean canSave) throws IOException {
        return new DriverDialog(mRoot, driver, canSave);
    }

    @PostConstruct
    public void init() {
        super.init();

        NodeUtils.setupCellValueFactory(mCodeColumn, driver -> driver.primaryKeyProperty().asObject());
        NodeUtils.setupCellValueFactory(mNameColumn, Driver::nameProperty);
        NodeUtils.setupCellValueFactory(mSalaryColumn, driver -> driver.salaryProperty().asObject());
        NodeUtils.setupCellValueFactory(mHiringDateColumn, driver -> {
            SimpleStringProperty formattedHiringDate = new SimpleStringProperty();
            formattedHiringDate.set(Utils.formatDate(driver.getHiringDate()));
            return formattedHiringDate;
        });
    }
}
