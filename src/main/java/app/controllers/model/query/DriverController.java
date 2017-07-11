package app.controllers.model.query;

import app.controllers.MainController;
import app.views.dialogs.DriverDialog;
import app.views.dialogs.ModelDialog;
import app.views.textfields.FloatTextField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import database.Database;
import database.QueryBuilder;
import database.model.Driver;
import io.datafx.controller.ViewController;
import javafx.beans.property.SimpleStringProperty;
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
    private JFXTextField mSearchCode;
    @FXML
    private JFXTextField mSearchName;
    @FXML
    private FloatTextField mSearchSalary;

    @FXML
    private JFXTreeTableColumn<Driver, Number> mCodeColumn;
    @FXML
    private JFXTreeTableColumn<Driver, String> mNameColumn;
    @FXML
    private JFXTreeTableColumn<Driver, Number> mSalaryColumn;
    @FXML
    private JFXTreeTableColumn<Driver, String> mHiringDateColumn;

    @Override
    QueryBuilder<Driver> getSearchQuery() {
        QueryBuilder<Driver> query = Database.from(Driver.class)
                .select("*");

        String searchCode = mSearchCode.getText();
        if (!Utils.isEmpty(searchCode))
            query.where("codigo", "=", Integer.valueOf(searchCode));

        String searchName = mSearchName.getText();
        if (!Utils.isEmpty(searchName))
            query.where("nome", "LIKE", "%" + searchName + "%");

        String searchSalary = mSearchSalary.getText();
        if (!Utils.isEmpty(searchSalary))
            query.where("salario", "=", Float.valueOf(searchSalary));

        return query;
    }

    @Override
    void clearQuery() {
        mSearchCode.setText("");
        mSearchName.setText("");
        mSearchSalary.setText("");
    }

    @Override
    ModelDialog getModelDialog(Driver driver, boolean canSave) throws IOException {
        return new DriverDialog(mRoot, driver, canSave);
    }

    @PostConstruct
    public void init() {
        super.init();

        MainController.setTitle("Motoristas");

        NodeUtils.setupCellValueFactory(mCodeColumn, Driver::primaryKeyProperty);
        NodeUtils.setupCellValueFactory(mNameColumn, Driver::nameProperty);
        NodeUtils.setupCellValueFactory(mSalaryColumn, Driver::salaryProperty);
        NodeUtils.setupCellValueFactory(mHiringDateColumn, driver -> {
            SimpleStringProperty formattedHiringDate = new SimpleStringProperty();
            formattedHiringDate.set(Utils.formatDate(driver.getHiringDate()));
            return formattedHiringDate;
        });
    }
}
