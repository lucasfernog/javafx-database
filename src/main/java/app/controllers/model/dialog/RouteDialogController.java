package app.controllers.model.dialog;

import app.controllers.MainController;
import app.views.AddEditComboBox;
import app.views.ButtonColumn;
import app.views.dialogs.BusStopDialog;
import app.views.dialogs.RouteDialog;
import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import database.model.BusStop;
import database.model.Itinerary;
import database.model.Model;
import database.model.Route;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import util.NodeUtils;

import java.io.IOException;
import java.util.List;

public class RouteDialogController extends ModelDialogController<Route> {

    @FXML
    private JFXTextField mDescription;
    @FXML
    private AddEditComboBox<Route, RouteDialogController> mReverseRoute;

    //Itinerário
    private ObservableList<Itinerary> mItineraries = FXCollections.observableArrayList();
    @FXML
    private JFXTreeTableView<Itinerary> mItineraryTable;
    @FXML
    private JFXTreeTableColumn<Itinerary, Number> mItineraryOrderColumn;
    @FXML
    private JFXTreeTableColumn<Itinerary, String> mItineraryStreetColumn;
    @FXML
    private JFXTreeTableColumn<Itinerary, Number> mItineraryStartBlockColumn;
    @FXML
    private JFXTreeTableColumn<Itinerary, Number> mItineraryEndBlockColumn;
    @FXML
    private ButtonColumn<Itinerary> mItineraryRemoveColumn;
    @FXML
    private JFXButton mAddItineraryButton;

    //Parada
    private ObservableList<BusStop> mBusStops = FXCollections.observableArrayList();
    @FXML
    private AddEditComboBox<BusStop, BusStopDialogController> mBusStop;
    @FXML
    private JFXTreeTableView<BusStop> mBusStopTable;
    @FXML
    private JFXTreeTableColumn<BusStop, Number> mBusStopCEPColumn;
    @FXML
    private JFXTreeTableColumn<BusStop, String> mBusStopStreetColumn;
    @FXML
    private JFXTreeTableColumn<BusStop, Number> mBusStopNumberColumn;
    @FXML
    private ButtonColumn<BusStop> mBusStopEditColumn;
    @FXML
    private ButtonColumn<BusStop> mBusStopRemoveColumn;

    @Override
    Model.SaveCallback saveModel() {
        if (mModel == null)
            mModel = new Route();

        if (!NodeUtils.validateRequiredTextFields(mDescription))
            return null;

        for (Itinerary itinerary : mItineraries)
            if (itinerary.getStreet().isEmpty())
                return null;

        mModel.setName(mDescription.getText());
        Route reverseRoute = mReverseRoute.getSelectionModel().getSelectedItem();
        if (reverseRoute != null)
            mModel.setReverseRouteId(reverseRoute.getPrimaryKey());
        mModel.setItineraries(mItineraries);

        mModel.setBusStops(mBusStops);

        return mModel.save();
    }

    @Override
    public void loadModel(boolean canSave) {
        Route route = getModel();

        mDescription.setText(route.getName());
        int reverseRouteId = route.getReverseRouteId();
        if (reverseRouteId > 0)
            NodeUtils.selectItemById(mReverseRoute, reverseRouteId);

        //Itinerários
        route.getItineraries().addListener((ListChangeListener<Itinerary>) c -> {
            while (c.next())
                mItineraries.addAll(c.getAddedSubList());
        });

        //Paradas
        route.getBusStops().addListener((ListChangeListener<BusStop>) c -> {
            while (c.next()) {
                List<? extends BusStop> added = c.getAddedSubList();
                for (BusStop busStop : added) {
                    mBusStops.add(busStop);
                    NodeUtils.removeItemById(mBusStop, busStop.getPrimaryKey());
                }
            }
        });

        if (!canSave)
            NodeUtils.disableAll(mDescription, mReverseRoute, mBusStop);
    }

    @Override
    public void initialize() {
        super.initialize();

        NodeUtils.setupRequiredTextFields(mDescription);

        //Linha contrária
        mReverseRoute.setDialogSupplier(item -> new RouteDialog(MainController.getRoot(), item));
        mReverseRoute.setItems(Route.getAll());
        mReverseRoute.getItems().add(0, new Route());

        //Itinerários
        mAddItineraryButton.setOnAction(e -> {
            Itinerary itinerary = new Itinerary();
            itinerary.setOrder(mItineraries.size() + 1);
            mItineraries.add(itinerary);
        });

        mItineraryRemoveColumn.setOnButtonClickListener(index -> {
            mItineraries.remove(index);
            mItineraryTable.setRoot(new RecursiveTreeItem<>(mItineraries, RecursiveTreeObject::getChildren));
        });

        mItineraryTable.setRoot(new RecursiveTreeItem<>(mItineraries, RecursiveTreeObject::getChildren));
        mItineraryTable.setShowRoot(false);

        NodeUtils.setupCellValueFactory(mItineraryStreetColumn, Itinerary::streetProperty);
        NodeUtils.setupEditableColumn(mItineraryStreetColumn, Itinerary::streetProperty);

        NodeUtils.setupCellValueFactory(mItineraryOrderColumn, Itinerary::orderProperty);
        NodeUtils.setupEditableColumn(mItineraryOrderColumn, Itinerary::orderProperty);

        NodeUtils.setupCellValueFactory(mItineraryStartBlockColumn, Itinerary::startBlockProperty);
        NodeUtils.setupEditableColumn(mItineraryStartBlockColumn, Itinerary::startBlockProperty);

        NodeUtils.setupCellValueFactory(mItineraryEndBlockColumn, Itinerary::endBlockProperty);
        NodeUtils.setupEditableColumn(mItineraryEndBlockColumn, Itinerary::endBlockProperty);

        //Paradas
        mBusStop.setDialogSupplier(item -> new BusStopDialog(MainController.getRoot(), item));
        mBusStop.setItems(BusStop.getAll());
        mBusStop.getItems().add(0, new BusStop());
        mBusStop.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.getPrimaryKey() > 0) {
                mBusStops.add(newValue);
                mBusStop.getItems().remove(newValue);
                mBusStop.hide(); //workaround
            }
        });

        mBusStopRemoveColumn.setOnButtonClickListener(index -> {
            mBusStop.getItems().add(mBusStops.get(index));
            mBusStops.remove(index);
        });
        mBusStopEditColumn.setOnButtonClickListener(index -> {
            try {
                new BusStopDialog(MainController.getRoot(), mBusStops.get(index)).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        mBusStopTable.setRoot(new RecursiveTreeItem<>(mBusStops, RecursiveTreeObject::getChildren));
        mBusStopTable.setShowRoot(false);

        NodeUtils.setupCellValueFactory(mBusStopCEPColumn, BusStop::cepProperty);
        NodeUtils.setupCellValueFactory(mBusStopStreetColumn, BusStop::streetProperty);
        NodeUtils.setupCellValueFactory(mBusStopNumberColumn, BusStop::numberProperty);

    }
}
