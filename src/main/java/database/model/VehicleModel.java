package database.model;

import database.Database;
import database.RowMap;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class VehicleModel extends NonCompositePrimaryKeyModel<VehicleModel> {

    private SimpleStringProperty mName = new SimpleStringProperty("");
    private SimpleIntegerProperty mManufacturerId = new SimpleIntegerProperty();

    private Manufacturer mManufacturer;

    public static ObservableList<VehicleModel> getAll() {
        ObservableList<VehicleModel> list = FXCollections.observableArrayList();

        Database.from(VehicleModel.class)
                .select("codigo", "fabricante", "nome")
                .execute(new Database.Callback<VehicleModel>() {
                    public void onSuccess(VehicleModel vehicleModel) {
                        list.add(vehicleModel);
                    }
                });

        return list;
    }

    @Override
    public String getTableName() {
        return "modelos";
    }

    @Override
    public VehicleModel from(RowMap rowMap) {
        VehicleModel vehicleModel = new VehicleModel();

        vehicleModel.setPrimaryKey(rowMap.getAsInteger("codigo"));
        vehicleModel.setName(rowMap.getAsString("nome"));
        vehicleModel.setManufacturerId(rowMap.getAsInteger("fabricante"));

        return vehicleModel;
    }

    @Override
    RowMap getValues() {
        RowMap row = new RowMap();

        row.put("nome", getName());
        row.put("fabricante", getManufacturerId());

        return row;
    }

    public String getName() {
        return nameProperty().get();
    }

    public void setName(String name) {
        nameProperty().set(name);
    }

    public SimpleStringProperty nameProperty() {
        return mName;
    }

    public int getManufacturerId() {
        return manufacturerProperty().get();
    }

    public void setManufacturerId(int manufacturerId) {
        manufacturerProperty().set(manufacturerId);
    }

    public SimpleIntegerProperty manufacturerProperty() {
        return mManufacturerId;
    }

    public Manufacturer getManufacturer() {
        return mManufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        mManufacturer = manufacturer;
    }

    @Override
    public String toString() {
        return getName();
    }
}
