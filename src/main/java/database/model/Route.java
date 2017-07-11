package database.model;

import database.Database;
import database.RowMap;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class Route extends NonCompositePrimaryKeyModel<Route> {

    private ObservableList<Itinerary> mItineraries;
    private ObservableList<Itinerary> mNewItineraries;
    private ObservableList<BusStop> mBusStops;
    private ObservableList<BusStop> mNewBusStops;
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

    @Override
    void onSave(Integer result) {
        super.onSave(result);

        int id = getPrimaryKey();

        if (mNewItineraries != null) {
            int index = 1;
            for (Itinerary itinerary : mNewItineraries)
                itinerary.setRouteId(id);
        }

        List<RouteBusStop> oldRouteBusStops = new ArrayList<>();
        List<RouteBusStop> newRouteBusStops = new ArrayList<>();

        if (mBusStops != null)
            for (BusStop busStop : mBusStops)
                oldRouteBusStops.add(new RouteBusStop(id, busStop.getPrimaryKey()));

        if (mNewBusStops != null)
            for (BusStop busStop : mNewBusStops)
                newRouteBusStops.add(new RouteBusStop(id, busStop.getPrimaryKey()));

        sync(mItineraries, mNewItineraries);
        sync(oldRouteBusStops, newRouteBusStops);
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

    public ObservableList<Itinerary> getItineraries() {
        return mItineraries = Itinerary.getAll(getPrimaryKey());
    }

    public ObservableList<BusStop> getBusStops() {
        mBusStops = FXCollections.observableArrayList();

        Database.from(BusStop.class)
                .select("codigo", "cep", "rua", "numero")
                .innerJoin("paradas_linhas", "paradas_linhas.parada", "=", "paradas.codigo")
                .where("linha", "=", getPrimaryKey())
                .execute(new Database.Callback<BusStop>() {
                    public void onSuccess(BusStop busStop) {
                        mBusStops.add(busStop);
                    }
                });

        return mBusStops;
    }

    public void setItineraries(ObservableList<Itinerary> itineraries) {
        mNewItineraries = itineraries;
    }

    public void setBusStops(ObservableList<BusStop> busStops) {
        mNewBusStops = busStops;
    }

    @Override
    public String toString() {
        return getName();
    }
}
