package database.model;

import database.RowMap;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Vehicle extends Model<Vehicle> {
    private SimpleStringProperty mLicensePlate = new SimpleStringProperty();
    private SimpleIntegerProperty mManufacturerId = new SimpleIntegerProperty();
    private SimpleStringProperty mModel = new SimpleStringProperty();

    @Override
    public String getTableName() {
        return "veiculos";
    }

    @Override
    public Vehicle from(RowMap rowMap) {
        Vehicle vehicle = new Vehicle();

        vehicle.setPrimaryKey(rowMap.getAsInteger("codigo"));
        vehicle.setLicensePlate(rowMap.getAsString("placa"));
        vehicle.setManufacturerId(rowMap.getAsInteger("fabricante"));
        vehicle.setModel(rowMap.getAsString("modelo"));

        return vehicle;
    }

    @Override
    RowMap getValues() {
        RowMap row = new RowMap();

        row.put("placa", getLicensePlate());
        row.put("fabricante", getManufacturerId());
        row.put("modelo", getModel());

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

    public int getManufacturerId() {
        return manufacturerIdProperty().get();
    }

    public void setManufacturerId(int manufacturerId) {
        manufacturerIdProperty().set(manufacturerId);
    }

    public SimpleIntegerProperty manufacturerIdProperty() {
        return mManufacturerId;
    }

    public String getModel() {
        return modelProperty().get();
    }

    public void setModel(String model) {
        modelProperty().set(model);
    }

    public SimpleStringProperty modelProperty() {
        return mModel;
    }
}
