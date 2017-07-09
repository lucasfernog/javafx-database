package database.model;

import database.Database;
import database.RowMap;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class BusStop extends NonCompositePrimaryKeyModel<BusStop> {

    private SimpleIntegerProperty mCEP = new SimpleIntegerProperty();
    private SimpleStringProperty mStreet = new SimpleStringProperty("");
    private SimpleIntegerProperty mNumber = new SimpleIntegerProperty();
    private boolean mTouched = false;

    public static ObservableList<BusStop> getAll() {
        ObservableList<BusStop> list = FXCollections.observableArrayList();

        Database.from(BusStop.class)
                .select("codigo", "cep", "rua", "numero")
                .execute(new Database.Callback<BusStop>() {
                    public void onSuccess(BusStop busStop) {
                        list.add(busStop);
                    }
                });

        return list;
    }

    @Override
    public String getTableName() {
        return "paradas";
    }

    @Override
    public BusStop from(RowMap rowMap) {
        BusStop busStop = new BusStop();

        busStop.setPrimaryKey(rowMap.getAsInteger("codigo"));
        busStop.setCEP(rowMap.getAsInteger("cep"));
        busStop.setStreet(rowMap.getAsString("rua"));
        busStop.setNumber(rowMap.getAsInteger("numero"));

        return busStop;
    }

    @Override
    RowMap getValues() {
        RowMap row = new RowMap();

        row.put("cep", getCEP());
        row.put("rua", getStreet());
        row.put("numero", getNumber());

        return row;
    }

    public int getCEP() {
        return cepProperty().get();
    }

    public SimpleIntegerProperty cepProperty() {
        return mCEP;
    }

    public void setCEP(int cep) {
        cepProperty().set(cep);
        mTouched = true;
    }

    public String getStreet() {
        return streetProperty().get();
    }

    public void setStreet(String street) {
        streetProperty().set(street);
        mTouched = true;
    }

    public SimpleStringProperty streetProperty() {
        return mStreet;
    }

    public int getNumber() {
        return numberProperty().get();
    }

    public SimpleIntegerProperty numberProperty() {
        return mNumber;
    }

    public void setNumber(int number) {
        numberProperty().set(number);
        mTouched = true;
    }

    @Override
    public String toString() {
        if (mTouched)
            return getCEP() + " " + getStreet() + " " + getNumber();
        return "";
    }
}
