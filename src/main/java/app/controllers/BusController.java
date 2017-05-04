package app.controllers;

import app.views.ButtonColumn;
import app.views.dialogs.BusDialog;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import database.Database;
import database.model.Bus;
import io.datafx.controller.ViewController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import util.Utils;

import javax.annotation.PostConstruct;
import java.io.IOException;

@ViewController(value = "/fxml/bus.fxml")
public class BusController {

    @FXML
    private StackPane mRoot;

    @FXML
    private JFXTreeTableColumn<Bus, Integer> mCodeColumn;

    @FXML
    private ButtonColumn<Bus> mEditColumn;

    @FXML
    private JFXTreeTableView<Bus> mTable;

    @FXML
    private JFXButton mInsertButton;

    private void showBusDialog(Bus bus, boolean canSave) {
        try {
            new BusDialog(mRoot, bus, canSave).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void newBusDialog() {
        showBusDialog(null, true);
    }

    @PostConstruct
    public void init() {
        Utils.setupCellValueFactory(mCodeColumn, bus -> bus.codeProperty().asObject());

        mTable.setEditable(false);

        ObservableList<Bus> busList = FXCollections.observableArrayList();

        Database.from(Bus.class)
                .select("id", "code")
                .execute(new Database.Callback<Bus>() {
                    public void onSuccess(Bus bus) {
                        busList.add(bus);
                    }
                });

        mTable.setRoot(new RecursiveTreeItem<>(busList, RecursiveTreeObject::getChildren));
        mTable.setShowRoot(false);

        //Visualizar
        mTable.setOnMouseClicked((mouseEvent) -> {
            if (mouseEvent.getClickCount() == 2)
                showBusDialog(mTable.getSelectionModel().getSelectedItem().getValue(), false);
        });

        //Incluir
        mInsertButton.setOnAction(event -> newBusDialog());

        //Alterar
        mEditColumn.setOnButtonClickListener(index -> showBusDialog(busList.get(index), true));
    }
}
