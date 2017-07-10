package database.model;

import database.RowMap;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import util.Utils;

public class Schedule extends CompositePrimaryKeyModel<Schedule> {

    private SimpleStringProperty mTime = new SimpleStringProperty("", "saida");
    private SimpleIntegerProperty mScheduleTypeId = new SimpleIntegerProperty(0, "tipo");
    private SimpleIntegerProperty mVehicleId = new SimpleIntegerProperty();
    private SimpleIntegerProperty mRouteId = new SimpleIntegerProperty(0, "linha");
    private SimpleIntegerProperty mDriverId = new SimpleIntegerProperty();

    private ScheduleType mScheduleType;
    private Vehicle mVehicle;
    private Route mRoute;
    private Driver mDriver;

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

        //tipo
        String scheduleType = rowMap.getAsString("descricao_tipo");
        if (!Utils.isEmpty(scheduleType)) {
            schedule.mScheduleType = new ScheduleType();
            schedule.mScheduleType.setPrimaryKey(schedule.getScheduleTypeId());
            schedule.mScheduleType.setDescription(scheduleType);
        }

        //veículo
        String vehicleLicensePlate = rowMap.getAsString("placa");
        if (!Utils.isEmpty(vehicleLicensePlate)) {
            schedule.mVehicle = new Vehicle();
            schedule.mVehicle.setPrimaryKey(schedule.getVehicleId());
            schedule.mVehicle.setLicensePlate(vehicleLicensePlate);
        }

        //linha
        String routeName = rowMap.getAsString("nome_linha");
        if (!Utils.isEmpty(routeName)) {
            schedule.mRoute = new Route();
            schedule.mRoute.setPrimaryKey(schedule.getRouteId());
            schedule.mRoute.setName(routeName);
        }

        //motorista
        String driverName = rowMap.getAsString("nome_motorista");
        if (!Utils.isEmpty(driverName)) {
            schedule.mDriver = new Driver();
            schedule.mDriver.setPrimaryKey(schedule.getDriverId());
            schedule.mDriver.setName(driverName);
        }

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

    public ScheduleType getScheduleType() {
        return mScheduleType;
    }

    public Vehicle getVehicle() {
        return mVehicle;
    }

    public Route getRoute() {
        return mRoute;
    }

    public Driver getDriver() {
        return mDriver;
    }
}
