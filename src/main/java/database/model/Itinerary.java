package database.model;

import database.Database;
import database.RowMap;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Itinerary extends NonCompositePrimaryKeyModel<Itinerary> {

    private SimpleIntegerProperty mRouteId = new SimpleIntegerProperty();
    private SimpleStringProperty mStreet = new SimpleStringProperty("");
    private SimpleIntegerProperty mStartBlock = new SimpleIntegerProperty();
    private SimpleIntegerProperty mEndBlock = new SimpleIntegerProperty();
    private SimpleIntegerProperty mOrder = new SimpleIntegerProperty();

    public static ObservableList<Itinerary> getAll(int routeId) {
        ObservableList<Itinerary> list = FXCollections.observableArrayList();

        Database.from(Itinerary.class)
                .select("*")
                .where("linha", "=", routeId)
                .execute(new Database.Callback<Itinerary>() {
                    public void onSuccess(Itinerary itinerary) {
                        list.add(itinerary);
                    }
                });

        return list;
    }

    @Override
    public String getTableName() {
        return "itinerarios";
    }

    @Override
    public Itinerary from(RowMap rowMap) {
        Itinerary itinerary = new Itinerary();

        itinerary.setPrimaryKey(rowMap.getAsInteger("codigo"));
        itinerary.setRouteId(rowMap.getAsInteger("linha"));
        itinerary.setStreet(rowMap.getAsString("rua"));
        itinerary.setStartBlock(rowMap.getAsInteger("inicio"));
        itinerary.setEndBlock(rowMap.getAsInteger("fim"));
        itinerary.setOrder(rowMap.getAsInteger("ordem"));

        return itinerary;
    }

    @Override
    RowMap getValues() {
        RowMap row = new RowMap();

        row.put("linha", getRouteId());
        row.put("rua", getStreet());
        row.put("inicio", getStartBlock());
        row.put("fim", getEndBlock());
        row.put("ordem", getOrder());

        return row;
    }

    public int getRouteId() {
        return routeIdProperty().get();
    }

    public void setRouteId(int routeId) {
        routeIdProperty().set(routeId);
    }

    public SimpleIntegerProperty routeIdProperty() {
        return mRouteId;
    }

    public String getStreet() {
        return streetProperty().get();
    }

    public void setStreet(String street) {
        streetProperty().set(street);
    }

    public SimpleStringProperty streetProperty() {
        return mStreet;
    }

    public int getStartBlock() {
        return startBlockProperty().get();
    }

    public void setStartBlock(int startBlock) {
        startBlockProperty().set(startBlock);
    }

    public SimpleIntegerProperty startBlockProperty() {
        return mStartBlock;
    }

    public int getEndBlock() {
        return endBlockProperty().get();
    }

    public void setEndBlock(int endBlock) {
        endBlockProperty().set(endBlock);
    }

    public SimpleIntegerProperty endBlockProperty() {
        return mEndBlock;
    }

    public int getOrder() {
        return orderProperty().get();
    }

    public void setOrder(int order) {
        orderProperty().set(order);
    }

    public SimpleIntegerProperty orderProperty() {
        return mOrder;
    }
}
