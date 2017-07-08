package database.model;

import database.Database;
import database.RowMap;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Route extends NonCompositePrimaryKeyModel<Route> {
    private SimpleStringProperty mName = new SimpleStringProperty("");

    private SimpleIntegerProperty mReverseRouteId = new SimpleIntegerProperty();

    public static ObservableList<Route> getAll() {
        ObservableList<Route> list = FXCollections.observableArrayList();

        Database.from(Route.class)
                .select("codigo", "nome", "linha_contraria")
                .execute(new Database.Callback<Route>() {
                    public void onSuccess(Route route) {
                        list.add(route);
                    }
                });

        return list;
    }

    @Override
    public String getTableName() {
        return "linhas";
    }

    @Override
    public Route from(RowMap rowMap) {
        Route route = new Route();

        route.setPrimaryKey(rowMap.getAsInteger("codigo"));
        route.setName(rowMap.getAsString("nome"));
        route.setReverseRouteId(rowMap.getAsInteger("linha_contraria"));

        return route;
    }

    @Override
    RowMap getValues() {
        RowMap row = new RowMap();

        row.put("nome", getName());
        int reverseRouteId = getReverseRouteId();
        row.put("linha_contraria", reverseRouteId > 0 ? reverseRouteId : null);

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

    public int getReverseRouteId() {
        return reverseRouteIdProperty().get();
    }

    public void setReverseRouteId(int reverseRouteId) {
        reverseRouteIdProperty().set(reverseRouteId);
    }

    public SimpleIntegerProperty reverseRouteIdProperty() {
        return mReverseRouteId;
    }

    @Override
    public String toString() {
        return getName();
    }
}
