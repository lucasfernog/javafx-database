package app.controllers.model.query;

import app.views.dialogs.ModelDialog;
import app.views.dialogs.ScheduleDialog;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTimePicker;
import com.jfoenix.controls.JFXTreeTableColumn;
import database.Database;
import database.QueryBuilder;
import database.model.*;
import io.datafx.controller.ViewController;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import util.NodeUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalTime;

@ViewController(value = "/fxml/schedules.fxml")
public class ScheduleController extends ModelQueryController<Schedule> {

    @FXML
    private StackPane mRoot;

    @FXML
    private JFXTimePicker mSearchTime;
    @FXML
    private JFXComboBox<ScheduleType> mSearchType;
    @FXML
    private JFXComboBox<Vehicle> mSearchVehicle;
    @FXML
    private JFXComboBox<Route> mSearchRoute;
    @FXML
    private JFXComboBox<Driver> mSearchDriver;

    @FXML
    private JFXTreeTableColumn<Schedule, String> mTypeColumn;
    @FXML
    private JFXTreeTableColumn<Schedule, String> mTimeColumn;
    @FXML
    private JFXTreeTableColumn<Schedule, String> mVehicleColumn;
    @FXML
    private JFXTreeTableColumn<Schedule, String> mRouteColumn;
    @FXML
    private JFXTreeTableColumn<Schedule, String> mDriverColumn;

    @Override
    QueryBuilder<Schedule> getSearchQuery() {
        QueryBuilder<Schedule> query = Database.from(Schedule.class)
                .select("saida", "tipo", "veiculo", "linha", "motorista",
                        "tipos.descricao as descricao_tipo",
                        "veiculos.placa as placa",
                        "linhas.nome as nome_linha",
                        "motoristas.nome as nome_motorista")
                .innerJoin("tipos", "tipos.codigo", "=", "tipo")
                .innerJoin("veiculos", "veiculos.codigo", "=", "veiculo")
                .innerJoin("linhas", "linhas.codigo", "=", "linha")
                .innerJoin("motoristas", "motoristas.codigo", "=", "motorista");

        //saída
        LocalTime searchTime = mSearchTime.getValue();
        if (searchTime != null)
            query.where("saida", "=", searchTime.toString());

        //tipo
        ScheduleType searchType = mSearchType.getSelectionModel().getSelectedItem();
        if (searchType != null) {
            int searchScheduleTypeId = searchType.getPrimaryKey();
            if (searchScheduleTypeId > 0)
                query.where("tipo", "=", searchScheduleTypeId);
        }

        //veículo
        Vehicle searchVehicle = mSearchVehicle.getSelectionModel().getSelectedItem();
        if (searchVehicle != null) {
            int searchVehicleId = searchVehicle.getPrimaryKey();
            if (searchVehicleId > 0)
                query.where("veiculo", "=", searchVehicleId);
        }

        //linha
        Route searchRoute = mSearchRoute.getSelectionModel().getSelectedItem();
        if (searchRoute != null) {
            int searchRouteId = searchRoute.getPrimaryKey();
            if (searchRouteId > 0)
                query.where("linha", "=", searchRouteId);
        }

        //motorista
        Driver searchDriver = mSearchDriver.getSelectionModel().getSelectedItem();
        if (searchDriver != null) {
            int searchDriverId = searchDriver.getPrimaryKey();
            if (searchDriverId > 0)
                query.where("motorista", "=", searchDriverId);
        }

        return query;
    }

    @Override
    void clearQuery() {
        mSearchTime.setValue(null);
        mSearchType.getSelectionModel().clearSelection();
        mSearchVehicle.getSelectionModel().clearSelection();
        mSearchRoute.getSelectionModel().clearSelection();
        mSearchDriver.getSelectionModel().clearSelection();
    }

    @Override
    ModelDialog getModelDialog(Schedule schedule, boolean canSave) throws IOException {
        return new ScheduleDialog(mRoot, schedule, canSave);
    }

    @PostConstruct
    public void init() {
        super.init();

        //tipo
        mSearchType.setItems(ScheduleType.getAll());
        mSearchType.getItems().add(0, new ScheduleType());
        //veículo
        mSearchVehicle.setItems(Vehicle.getAll());
        mSearchVehicle.getItems().add(0, new Vehicle());
        //linha
        mSearchRoute.setItems(Route.getAll());
        mSearchRoute.getItems().add(0, new Route());
        //motorista
        mSearchDriver.setItems(Driver.getAll());
        mSearchDriver.getItems().add(0, new Driver());

        NodeUtils.setupCellValueFactory(mTypeColumn, schedule -> schedule.getScheduleType().descriptionProperty());
        NodeUtils.setupCellValueFactory(mTimeColumn, Schedule::timeProperty);
        NodeUtils.setupCellValueFactory(mVehicleColumn, schedule -> schedule.getVehicle().licensePlateProperty());
        NodeUtils.setupCellValueFactory(mRouteColumn, schedule -> schedule.getRoute().nameProperty());
        NodeUtils.setupCellValueFactory(mDriverColumn, schedule -> schedule.getDriver().nameProperty());
    }
}
