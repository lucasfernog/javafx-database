package database.model;

import database.RowMap;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Vehicle extends Model<Vehicle> {
    private SimpleStringProperty mLicensePlate = new SimpleStringProperty();
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

        return vehicle;
    }

    @Override
    RowMap getValues() {
        RowMap row = new RowMap();

        row.put("placa", getLicensePlate());
        row.put("modelo", getModelId());

        return row;
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
}
