package app.controllers.model.dialog;

import app.controllers.MainController;
import app.views.AddEditComboBox;
import app.views.dialogs.DriverDialog;
import app.views.dialogs.RouteDialog;
import app.views.dialogs.VehicleDialog;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTimePicker;
import database.model.*;
import javafx.fxml.FXML;
import util.NodeUtils;

import java.time.LocalTime;

public class ScheduleDialogController extends ModelDialogController<Schedule> {

    @FXML
    private JFXTimePicker mTime;

    @FXML
    private JFXComboBox<ScheduleType> mScheduleType;

    @FXML
    private AddEditComboBox<Vehicle, VehicleDialogController> mVehicle;

    @FXML
    private AddEditComboBox<Route, RouteDialogController> mRoute;

    @FXML
    private AddEditComboBox<Driver, DriverDialogController> mDriver;

    @Override
    Model.SaveCallback saveModel() {
        if (mModel == null)
            mModel = new Schedule();


        if (!NodeUtils.validateRequired(mTime, mScheduleType, mVehicle, mRoute, mDriver))
            return null;

        mModel.setTime(mTime.getValue().toString());
        mModel.setScheduleTypeId(mScheduleType.getSelectionModel().getSelectedItem().getPrimaryKey());
        mModel.setVehicleId(mVehicle.getSelectionModel().getSelectedItem().getPrimaryKey());
        mModel.setRouteId(mRoute.getSelectionModel().getSelectedItem().getPrimaryKey());
        mModel.setDriverId(mDriver.getSelectionModel().getSelectedItem().getPrimaryKey());

        return mModel.save();
    }

    @Override
    public void loadModel(boolean canSave) {
        Schedule schedule = getModel();

        mTime.setValue(LocalTime.parse(schedule.getTime()));
        NodeUtils.selectItemById(mScheduleType, schedule.getScheduleTypeId());
        NodeUtils.selectItemById(mVehicle, schedule.getVehicleId());
        NodeUtils.selectItemById(mRoute, schedule.getRouteId());
        NodeUtils.selectItemById(mDriver, schedule.getDriverId());

        if (!canSave)
            NodeUtils.disableAll(mTime, mScheduleType, mVehicle, mRoute, mDriver);
    }

    @Override
    public void initialize() {
        super.initialize();

        mScheduleType.setItems(ScheduleType.getAll());
        mScheduleType.getItems().add(0, new ScheduleType());

        mVehicle.setSuppliers(item -> new VehicleDialog(MainController.getRoot(), item), Vehicle::new);
        mVehicle.setItems(Vehicle.getAll());
        mVehicle.getItems().add(0, new Vehicle());

        mRoute.setSuppliers(item -> new RouteDialog(MainController.getRoot(), item), Route::new);
        mRoute.setItems(Route.getAll());
        mRoute.getItems().add(0, new Route());

        mDriver.setSuppliers(item -> new DriverDialog(MainController.getRoot(), item), Driver::new);
        mDriver.setItems(Driver.getAll());
        mDriver.getItems().add(0, new Driver());
    }
}
