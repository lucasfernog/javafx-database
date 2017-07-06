package app.controllers.model.query;

import app.views.ButtonColumn;
import app.views.dialogs.ModelDialog;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import database.model.Model;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import java.io.IOException;

public abstract class ModelQueryController<T extends Model<T>> {
    @FXML
    private JFXTreeTableView<T> mTable;

    @FXML
    private ButtonColumn<Model> mEditColumn;

    @FXML
    private JFXButton mInsertButton;

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

    protected void init() {
        mTable.setEditable(false);

        mTable.setRoot(new RecursiveTreeItem<>(getList(), RecursiveTreeObject::getChildren));
        mTable.setShowRoot(false);

        //Visualizar
        mTable.setOnMouseClicked((mouseEvent) -> {
            if (mouseEvent.getClickCount() == 2)
                showModelDialog(mTable.getSelectionModel().getSelectedItem().getValue(), false);
        });

        //Incluir
        mInsertButton.setOnAction(event -> newModelDialog());

        //Alterar
        mEditColumn.setOnButtonClickListener(index -> showModelDialog(getList().get(index), true));
    }

    protected JFXTreeTableView getTable() {
        return mTable;
    }
}
