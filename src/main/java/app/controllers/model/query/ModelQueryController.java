package app.controllers.model.query;

import app.controllers.MainController;
import app.views.ButtonColumn;
import app.views.dialogs.ModelDialog;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import database.Database;
import database.QueryBuilder;
import database.model.Model;
import javafx.application.Platform;
import javafx.collections.FXCollections;
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
    private ButtonColumn<Model> mRemoveColumn;

    @FXML
    private JFXButton mInsertButton;
    @FXML
    private JFXButton mSearchButton;
    @FXML
    private JFXButton mClearButton;

    private ObservableList<T> mList;

    abstract ModelDialog getModelDialog(T model, boolean canSave) throws IOException;

    abstract QueryBuilder<T> getSearchQuery();

    abstract void clearQuery();

    ModelQueryController() {
        MainController.closeDrawer();
    }

    Database.Callback<T> getCallback() {
        return new Database.Callback<T>() {
            boolean first = true;
            @Override
            public void onSuccess(T item) {
                if (first) {
                    first = false;
                    mList = FXCollections.observableArrayList();
                }
                mList.add(item);
            }

            @Override
            public void onFinish() {
                if (first)
                    mList = FXCollections.observableArrayList();
                Platform.runLater(() -> mTable.setRoot(new RecursiveTreeItem<>(mList, RecursiveTreeObject::getChildren)));
            }
        };
    }

    private void showModelDialog(T model, boolean canSave) {
        try {
            ModelDialog dialog = getModelDialog(model, canSave);
            dialog.setOnSaveListener(m -> search());
            dialog.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void newModelDialog() {
        showModelDialog(null, true);
    }

    private void setTableData() {
        getSearchQuery().execute(getCallback());
    }

    @FXML
    private void search() {
        setTableData();
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

        //Pesquisar
        mSearchButton.addEventHandler(ActionEvent.ACTION, event -> search());

        //Limpar
        mClearButton.addEventHandler(ActionEvent.ACTION, event -> {
            clearQuery();
            search();
        });

        //Alterar
        mEditColumn.setOnButtonClickListener(index -> showModelDialog(mList.get(index), true));
        //Remover
        mRemoveColumn.setOnButtonClickListener(index -> {
            mList.get(index).delete();
            mList.remove(index);
        });
    }

    protected JFXTreeTableView getTable() {
        return mTable;
    }
}
