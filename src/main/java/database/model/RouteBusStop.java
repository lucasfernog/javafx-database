package database.model;

import database.RowMap;
import javafx.beans.property.SimpleIntegerProperty;

public class RouteBusStop extends CompositePrimaryKeyModel<RouteBusStop> {

    private SimpleIntegerProperty mRouteId = new SimpleIntegerProperty(0, "linha");
    private SimpleIntegerProperty mBusStopId = new SimpleIntegerProperty(0, "parada");

    public RouteBusStop(int routeId, int busStopId) {
        setRouteId(routeId);
        setBusStopId(busStopId);
        savePreviousPrimaryKeyValues(routeIdProperty(), busStopIdProperty());
    }

    @Override
    public String getTableName() {
        return "paradas_linhas";
    }

    @Override
    public RouteBusStop from(RowMap rowMap) {
        return  new RouteBusStop(rowMap.getAsInteger("linha"), rowMap.getAsInteger("parada"));
    }

    @Override
    RowMap getValues() {
        RowMap row = new RowMap();

        row.put("linha", getRouteId());
        row.put("parada", getBusStopId());

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

    public int getBusStopId() {
        return busStopIdProperty().get();
    }

    public SimpleIntegerProperty busStopIdProperty() {
        return mBusStopId;
    }

    public void setBusStopId(int busStopId) {
        busStopIdProperty().set(busStopId);
    }

    public boolean equals(Object other) {
        if (other instanceof RouteBusStop) {
            RouteBusStop routeBusStop = (RouteBusStop) other;
            return routeBusStop.getBusStopId() == getBusStopId() && routeBusStop.getRouteId() == getRouteId();
        }

        return super.equals(other);
    }
}
