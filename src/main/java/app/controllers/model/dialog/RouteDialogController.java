package app.controllers.model.dialog;

import app.controllers.MainController;
import app.views.AddEditComboBox;
import app.views.ButtonColumn;
import app.views.dialogs.RouteDialog;
import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import database.model.Itinerary;
import database.model.Model;
import database.model.Route;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import util.NodeUtils;

public class RouteDialogController extends ModelDialogController<Route> {

    @FXML
    private JFXTextField mDescription;
    @FXML
    private AddEditComboBox<Route, RouteDialogController> mReverseRoute;
    @FXML
    private JFXTreeTableView<Itinerary> mTable;
    @FXML
    private JFXTreeTableColumn<Itinerary, String> mStreetColumn;
    @FXML
    private JFXTreeTableColumn<Itinerary, Integer> mStartBlockColumn;
    @FXML
    private JFXTreeTableColumn<Itinerary, Integer> mEndBlockColumn;
    @FXML
    private ButtonColumn<Itinerary> mRemoveColumn;
    @FXML
    private JFXButton mAddItineraryButton;
    private ObservableList<Itinerary> mItineraries;

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

        return mModel.save();
    }

    @Override
    public void loadModel(boolean canSave) {
        Route route = getModel();

        mDescription.setText(route.getName());
        int reverseRouteId = route.getReverseRouteId();
        if (reverseRouteId > 0)
            NodeUtils.selectItemById(mReverseRoute, reverseRouteId);

        route.getItineraries().addListener((ListChangeListener<Itinerary>) c -> {
            while (c.next())
                mItineraries.addAll(c.getAddedSubList());
        });

        if (!canSave)
            NodeUtils.disableAll(mDescription, mReverseRoute);
    }

    @Override
    public void initialize() {
        super.initialize();

        NodeUtils.setupRequiredTextFields(mDescription);

        mReverseRoute.setDialogSupplier(item -> new RouteDialog(MainController.getRoot(), item));
        mReverseRoute.setItems(Route.getAll());
        mReverseRoute.getItems().add(0, new Route());

        mAddItineraryButton.setOnAction(e -> mItineraries.add(new Itinerary()));

        mRemoveColumn.setOnButtonClickListener(index -> mItineraries.remove(index));

        mItineraries = FXCollections.observableArrayList();
        mTable.setRoot(new RecursiveTreeItem<>(mItineraries, RecursiveTreeObject::getChildren));
        mTable.setShowRoot(false);

        NodeUtils.setupCellValueFactory(mStreetColumn, Itinerary::streetProperty);
        NodeUtils.setupEditableColumn(mStreetColumn, Itinerary::streetProperty);

        NodeUtils.setupCellValueFactory(mStartBlockColumn, itinerary -> itinerary.startBlockProperty().asObject());
        NodeUtils.setupEditableColumn(mStartBlockColumn, itinerary -> itinerary.startBlockProperty().asObject());

        NodeUtils.setupCellValueFactory(mEndBlockColumn, itinerary -> itinerary.endBlockProperty().asObject());
        NodeUtils.setupEditableColumn(mEndBlockColumn, itinerary -> itinerary.endBlockProperty().asObject());
    }
}
