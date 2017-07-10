package app.controllers.model.query;

import app.views.dialogs.ModelDialog;
import app.views.dialogs.VehicleDialog;
import app.views.textfields.LicensePlateTextField;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import database.Database;
import database.QueryBuilder;
import database.model.Manufacturer;
import database.model.ScheduleType;
import database.model.Vehicle;
import database.model.VehicleModel;
import io.datafx.controller.ViewController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionModel;
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
    private JFXComboBox<Manufacturer> mSearchManufacturer;
    @FXML
    private JFXComboBox<VehicleModel> mSearchVehicleModel;
    @FXML
    private LicensePlateTextField mSearchLicensePlate;

    @FXML
    private JFXTreeTableColumn<Vehicle, Number> mCodeColumn;
    @FXML
    private JFXTreeTableColumn<Vehicle, String> mLicensePlateColumn;
    @FXML
    private JFXTreeTableColumn<Vehicle, String> mManufacturerColumn;
    @FXML
    private JFXTreeTableColumn<Vehicle, String> mVehicleModelColumn;

    @Override
    QueryBuilder<Vehicle> getSearchQuery() {
        QueryBuilder<Vehicle> query = Database.from(Vehicle.class)
                .select("veiculos.codigo", "placa", "modelo",
                        "modelos.nome as nome_modelo", "fabricantes.nome as nome_fabricante")
                .innerJoin("modelos", "modelos.codigo", "=", "modelo")
                .innerJoin("fabricantes", "fabricantes.codigo", "=", "fabricante");

        String searchCode = mSearchCode.getText();
        if (!Utils.isEmpty(searchCode))
            query.where("veiculos.codigo", "=", Integer.valueOf(searchCode));

        String searchLicensePlate = mSearchLicensePlate.removeMask();
        if (!Utils.isEmpty(searchLicensePlate))
            query.where("cast(placa as varchar(7))", "like", "%" + searchLicensePlate + "%");

        //fabricante
        Runnable manufacturerFilter = () -> {
            Manufacturer searchManufacturer = mSearchManufacturer.getSelectionModel().getSelectedItem();
            if (searchManufacturer != null) {
                int searchManufacturerId = searchManufacturer.getPrimaryKey();
                if (searchManufacturerId > 0)
                    query.where("fabricante", "=", searchManufacturerId);
            }
        };

        //modelo
        VehicleModel vehicleModel = mSearchVehicleModel.getSelectionModel().getSelectedItem();
        if (vehicleModel != null) {
            int searchVehicleModelId = vehicleModel.getPrimaryKey();
            if (searchVehicleModelId > 0)
                query.where("modelo", "=", searchVehicleModelId);
            else
                manufacturerFilter.run();
        } else
            manufacturerFilter.run();

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

        //fabricante
        mSearchManufacturer.setItems(Manufacturer.getAll());
        mSearchManufacturer.getItems().add(0, new Manufacturer());
        ObservableList<VehicleModel> vehicleModels = VehicleModel.getAll();
        vehicleModels.add(0, new VehicleModel());
        NodeUtils.bindChildComboBox(mSearchManufacturer, mSearchVehicleModel, vehicleModels, VehicleModel::manufacturerProperty);

        NodeUtils.setupCellValueFactory(mCodeColumn, Vehicle::primaryKeyProperty);
        NodeUtils.setupCellValueFactory(mLicensePlateColumn, vehicle -> {
            SimpleStringProperty formattedLicensePlate = new SimpleStringProperty();
            formattedLicensePlate.set(Utils.applyMask(Utils.LICENSE_PLATE_MASK, vehicle.getLicensePlate()));
            return formattedLicensePlate;
        });
        NodeUtils.setupCellValueFactory(mVehicleModelColumn, vehicle -> vehicle.getModel().nameProperty());
        NodeUtils.setupCellValueFactory(mManufacturerColumn, vehicle -> vehicle.getModel().getManufacturer().nameProperty());
    }
}
