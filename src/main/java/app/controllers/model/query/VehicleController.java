package app.controllers.model.query;

import app.views.dialogs.ModelDialog;
import app.views.dialogs.VehicleDialog;
import com.jfoenix.controls.JFXTreeTableColumn;
import database.Database;
import database.model.Vehicle;
import io.datafx.controller.ViewController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import util.NodeUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;

@ViewController(value = "/fxml/vehicle.fxml")
public class VehicleController extends ModelQueryController<Vehicle> {

    @FXML
    private StackPane mRoot;

    @FXML
    private JFXTreeTableColumn<Vehicle, Integer> mCodeColumn;

    @Override
    ObservableList<Vehicle> getList() {
        ObservableList<Vehicle> vehicleList = FXCollections.observableArrayList();

        Database.from(Vehicle.class)
                .select("codigo", "placa", "modelo")
                .execute(new Database.Callback<Vehicle>() {
                    public void onSuccess(Vehicle vehicle) {
                        vehicleList.add(vehicle);
                    }
                });

        return vehicleList;
    }

    @Override
    ModelDialog getModelDialog(Vehicle vehicle, boolean canSave) throws IOException {
        return new VehicleDialog(mRoot, vehicle, canSave);
    }

    @PostConstruct
    public void init() {
        super.init();

        NodeUtils.setupCellValueFactory(mCodeColumn, vehicle -> vehicle.primaryKeyProperty().asObject());
    }
}
