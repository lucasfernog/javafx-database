package database.model;

import database.RowMap;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Schedule extends CompositePrimaryKeyModel<Schedule> {

    private SimpleStringProperty mTime = new SimpleStringProperty("", "saida");
    private SimpleIntegerProperty mScheduleTypeId = new SimpleIntegerProperty(0, "tipo");
    private SimpleIntegerProperty mVehicleId = new SimpleIntegerProperty();
    private SimpleIntegerProperty mRouteId = new SimpleIntegerProperty(0, "linha");
    private SimpleIntegerProperty mDriverId = new SimpleIntegerProperty();

    @Override
    public String getTableName() {
        return "horarios";
    }

    @Override
    public Schedule from(RowMap rowMap) {
        Schedule schedule = new Schedule();

        schedule.setTime(rowMap.getAsString("saida"));
        schedule.setScheduleTypeId(rowMap.getAsInteger("tipo"));
        schedule.setVehicleId(rowMap.getAsInteger("veiculo"));
        schedule.setRouteId(rowMap.getAsInteger("linha"));
        schedule.setDriverId(rowMap.getAsInteger("motorista"));

        schedule.savePreviousPrimaryKeyValues(schedule.timeProperty(), schedule.scheduleTypeIdProperty(), schedule.routeIdProperty());

        return schedule;
    }

    @Override
    RowMap getValues() {
        RowMap row = new RowMap();

        row.put("saida", getTime());
        row.put("tipo", getScheduleTypeId());
        row.put("veiculo", getVehicleId());
        row.put("linha", getRouteId());
        row.put("motorista", getDriverId());

        return row;
    }

    public String getTime() {
        return timeProperty().get();
    }

    public void setTime(String time) {
        timeProperty().set(time);
    }

    public SimpleStringProperty timeProperty() {
        return mTime;
    }

    public int getScheduleTypeId() {
        return scheduleTypeIdProperty().get();
    }

    public void setScheduleTypeId(int scheduleTypeId) {
        scheduleTypeIdProperty().set(scheduleTypeId);
    }

    public SimpleIntegerProperty scheduleTypeIdProperty() {
        return mScheduleTypeId;
    }

    public int getVehicleId() {
        return vehicleIdProperty().get();
    }

    public void setVehicleId(int vehicleId) {
        vehicleIdProperty().set(vehicleId);
    }

    public SimpleIntegerProperty vehicleIdProperty() {
        return mVehicleId;
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

    public int getDriverId() {
        return mDriverId.get();
    }

    public void setDriverId(int driverId) {
        driverIdProperty().set(driverId);
    }

    public SimpleIntegerProperty driverIdProperty() {
        return mDriverId;
    }
}
