package database.model;

import database.Database;
import database.RowMap;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Manufacturer extends NonCompositePrimaryKeyModel<Manufacturer> {
    private SimpleStringProperty mName = new SimpleStringProperty("");

    public static ObservableList<Manufacturer> getAll() {
        ObservableList<Manufacturer> list = FXCollections.observableArrayList();

        Database.from(Manufacturer.class)
                .select("codigo", "nome")
                .execute(new Database.Callback<Manufacturer>() {
                    public void onSuccess(Manufacturer manufacturer) {
                        list.add(manufacturer);
                    }
                });

        return list;
    }

    @Override
    public String getTableName() {
        return "fabricantes";
    }

    @Override
    public Manufacturer from(RowMap rowMap) {
        Manufacturer manufacturer = new Manufacturer();

        manufacturer.setPrimaryKey(rowMap.getAsInteger("codigo"));
        manufacturer.setName(rowMap.getAsString("nome"));

        return manufacturer;
    }

    @Override
    RowMap getValues() {
        RowMap row = new RowMap();

        row.put("nome", getName());

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

    @Override
    public String toString() {
        return getName();
    }
}
