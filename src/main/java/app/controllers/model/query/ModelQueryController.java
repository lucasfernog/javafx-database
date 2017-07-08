package app.controllers.model.query;

import app.views.ButtonColumn;
import app.views.dialogs.ModelDialog;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import database.model.Model;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public abstract class ModelQueryController<T extends Model<T>> {
    @FXML
    private JFXTreeTableView<T> mTable;

    @FXML
    private ButtonColumn<Model> mEditColumn;

    @FXML
    private JFXButton mInsertButton;

    @FXML
    private JFXButton mSearchButton;

    ObservableList<T> mList;

    abstract ModelDialog getModelDialog(T model, boolean canSave) throws IOException;

    abstract ObservableList<T> getList();

    private void showModelDialog(T model, boolean canSave) {
        try {
            getModelDialog(model, canSave).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void newModelDialog() {
        showModelDialog(null, true);
    }

    private void setTableData() {
        mList = getList();
        mTable.setRoot(new RecursiveTreeItem<>(mList, RecursiveTreeObject::getChildren));
    }

    protected void init() {
        mTable.setEditable(false);
        setTableData();
        mTable.setShowRoot(false);

        //Visualizar
        mTable.setOnMouseClicked((mouseEvent) -> {
            if (mouseEvent.getClickCount() == 2)
                showModelDialog(mTable.getSelectionModel().getSelectedItem().getValue(), false);
        });

        //Incluir
        mInsertButton.addEventHandler(ActionEvent.ACTION, event -> newModelDialog());

        mSearchButton.addEventHandler(ActionEvent.ACTION, event -> setTableData());

        //Alterar
        mEditColumn.setOnButtonClickListener(index -> showModelDialog(mList.get(index), true));
    }

    protected JFXTreeTableView getTable() {
        return mTable;
    }
}
