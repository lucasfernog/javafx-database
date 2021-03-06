package database.model;

import database.Database;
import database.RowMap;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.Utils;

public class Vehicle extends NonCompositePrimaryKeyModel<Vehicle> {
    private SimpleStringProperty mLicensePlate = new SimpleStringProperty("");
    private SimpleIntegerProperty mModelId = new SimpleIntegerProperty();
    private VehicleModel mModel;

    @Override
    public String getTableName() {
        return "veiculos";
    }

    @Override
    public Vehicle from(RowMap rowMap) {
        Vehicle vehicle = new Vehicle();

        vehicle.setPrimaryKey(rowMap.getAsInteger("codigo"));
        vehicle.setLicensePlate(rowMap.getAsString("placa"));
        vehicle.setModelId(rowMap.getAsInteger("modelo"));

        String modelName = rowMap.getAsString("nome_modelo");
        if (!Utils.isEmpty(modelName)) {
            vehicle.mModel = new VehicleModel();
            vehicle.mModel.setPrimaryKey(vehicle.getModelId());
            vehicle.mModel.setName(modelName);

            String manufacturerName = rowMap.getAsString("nome_fabricante");
            if (!Utils.isEmpty(manufacturerName)) {
                Manufacturer manufacturer = new Manufacturer();
                manufacturer.setName(manufacturerName);
                vehicle.mModel.setManufacturer(manufacturer);
            }
        }

        return vehicle;
    }

    @Override
    RowMap getValues() {
        RowMap row = new RowMap();

        row.put("placa", getLicensePlate());
        row.put("modelo", getModelId());

        return row;
    }

    public static ObservableList<Vehicle> getAll() {
        ObservableList<Vehicle> list = FXCollections.observableArrayList();

        Database.from(Vehicle.class)
                .select("codigo", "placa", "modelo")
                .execute(new Database.Callback<Vehicle>() {
                    public void onSuccess(Vehicle vehicle) {
                        list.add(vehicle);
                    }
                });

        return list;
    }

    public String getLicensePlate() {
        return licensePlateProperty().get();
    }

    public void setLicensePlate(String licensePlate) {
        licensePlateProperty().set(licensePlate);
    }

    public SimpleStringProperty licensePlateProperty() {
        return mLicensePlate;
    }

    public int getModelId() {
        return modelProperty().get();
    }

    public void setModelId(int modelId) {
        modelProperty().set(modelId);
    }

    public VehicleModel getModel() {
        return mModel;
    }

    public SimpleIntegerProperty modelProperty() {
        return mModelId;
    }

    @Override
    public String toString() {
        return Utils.applyMask(Utils.LICENSE_PLATE_MASK, getLicensePlate());
    }
}
