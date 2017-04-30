package app.controllers;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import database.model.Bus;
import io.datafx.controller.ViewController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import util.Utils;

import javax.annotation.PostConstruct;

@ViewController(value = "/fxml/bus.fxml")
public class BusController {

    @FXML
    private StackPane mRoot;

    @FXML
    private JFXTreeTableColumn<Bus, Integer> mCodeColumn;

    @FXML
    private JFXTreeTableView<Bus> mTable;

    @PostConstruct
    public void init() {
        Utils.setupCellValueFactory(mCodeColumn, bus -> bus.codeProperty().asObject());

        mTable.setEditable(false);

        ObservableList<Bus> busList = FXCollections.observableArrayList();
        busList.add(new Bus(1));
        busList.add(new Bus(2));
        mTable.setRoot(new RecursiveTreeItem<>(busList, RecursiveTreeObject::getChildren));

        mTable.setShowRoot(false);
    }
}
