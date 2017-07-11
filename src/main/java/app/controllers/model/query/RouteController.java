package app.controllers.model.query;

import app.views.dialogs.ModelDialog;
import app.views.dialogs.RouteDialog;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import database.Database;
import database.QueryBuilder;
import database.model.Route;
import database.model.Student;
import io.datafx.controller.ViewController;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import util.NodeUtils;
import util.Utils;

import javax.annotation.PostConstruct;
import java.io.IOException;

@ViewController(value = "/fxml/routes.fxml")
public class RouteController extends ModelQueryController<Route> {

    @FXML
    private StackPane mRoot;

    @FXML
    private JFXTextField mSearchCode;
    @FXML
    private JFXTextField mSearchName;
    @FXML
    private JFXTextField mSearchBusStopStreet;

    @FXML
    private JFXTreeTableColumn<Route, Number> mCodeColumn;
    @FXML
    private JFXTreeTableColumn<Route, String> mNameColumn;
    @FXML
    private JFXTreeTableColumn<Route, Number> mBalanceColumn;

    @Override
    QueryBuilder<Route> getSearchQuery() {
        QueryBuilder<Route> query = Database.from(Route.class)
                .select("*");

        String searchCode = mSearchCode.getText();
        if (!Utils.isEmpty(searchCode))
            query.where("codigo", "=", Integer.valueOf(searchCode));

        String searchName = mSearchName.getText();
        if (!Utils.isEmpty(searchName))
            query.where("nome", "ILIKE", "%" + searchName + "%");

        String searchBusStopStreet = mSearchBusStopStreet.getText();
        if (!Utils.isEmpty(searchBusStopStreet))
            query.whereExists((existsQuery) ->
                    existsQuery.select("codigo")
                            .from("paradas_linhas")
                            .innerJoin("paradas", "paradas.codigo", "=", "parada")
                            .where("rua", "ILIKE", "%" + searchBusStopStreet + "%")
                            .where("linha", "=", new Database.Column("codigo"))
            );

        return query;
    }

    @Override
    void clearQuery() {
        mSearchCode.setText("");
        mSearchName.setText("");
        mSearchBusStopStreet.setText("");
    }

    @Override
    ModelDialog getModelDialog(Route route, boolean canSave) throws IOException {
        return new RouteDialog(mRoot, route, canSave);
    }

    @PostConstruct
    public void init() {
        super.init();

        NodeUtils.setupCellValueFactory(mCodeColumn, Route::primaryKeyProperty);
        NodeUtils.setupCellValueFactory(mNameColumn, Route::nameProperty);
    }
}
