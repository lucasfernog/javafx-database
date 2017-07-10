package app.controllers.model.query;

import app.views.dialogs.ModelDialog;
import app.views.dialogs.VehicleDialog;
import app.views.textfields.LicensePlateTextField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import database.Database;
import database.QueryBuilder;
import database.model.Vehicle;
import io.datafx.controller.ViewController;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import util.NodeUtils;
import util.Utils;

import javax.annotation.PostConstruct;
import java.io.IOException;

@ViewController(value = "/fxml/vehicles.fxml")
public class VehicleController extends ModelQueryController<Vehicle> {

    @FXML
    private StackPane mRoot;

    @FXML
    private JFXTextField mSearchCode;
    @FXML
    private LicensePlateTextField mSearchLicensePlate;

    @FXML
    private JFXTreeTableColumn<Vehicle, Number> mCodeColumn;
    @FXML
    private JFXTreeTableColumn<Vehicle, String> mLicensePlateColumn;

    @Override
    QueryBuilder<Vehicle> getSearchQuery() {
        QueryBuilder<Vehicle> query = Database.from(Vehicle.class)
                .select("codigo", "placa", "modelo");

        String searchCode = mSearchCode.getText();
        if (!Utils.isEmpty(searchCode))
            query.where("codigo", "=", Integer.valueOf(searchCode));

        String searchLicensePlate = mSearchLicensePlate.removeMask();
        if (!Utils.isEmpty(searchLicensePlate))
            query.where("cast(placa as varchar(7))", "like", "%" + searchLicensePlate + "%");

        return query;
    }

    @Override
    void clearQuery() {
        mSearchCode.setText("");
        mSearchLicensePlate.setText("");
    }

    @Override
    ModelDialog getModelDialog(Vehicle vehicle, boolean canSave) throws IOException {
        return new VehicleDialog(mRoot, vehicle, canSave);
    }

    @PostConstruct
    public void init() {
        super.init();

        NodeUtils.setupCellValueFactory(mCodeColumn, Vehicle::primaryKeyProperty);
        NodeUtils.setupCellValueFactory(mLicensePlateColumn, vehicle -> {
            SimpleStringProperty formattedLicensePlate = new SimpleStringProperty();
            formattedLicensePlate.set(Utils.applyMask(Utils.LICENSE_PLATE_MASK, vehicle.getLicensePlate()));
            return formattedLicensePlate;
        });
    }
}
