package util;

import app.controllers.MainController;
import app.views.dialogs.AlertDialog;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.validation.RequiredFieldValidator;
import database.model.Model;
import database.model.NonCompositePrimaryKeyModel;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WritableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;

import java.util.List;
import java.util.function.Function;

public class NodeUtils {

    public static <T extends NonCompositePrimaryKeyModel> void selectItemById(ComboBox<T> comboBox, int id) {
        Platform.runLater(() -> {
            ObservableList<T> list = comboBox.getItems();
            T item = Utils.find(list, id);
            if (item != null)
                comboBox.getSelectionModel().select(item);
            else
                list.addListener(new ListChangeListener<T>() {
                    @Override
                    public void onChanged(Change<? extends T> change) {
                        while (change.next()) {
                            List<? extends T> added = change.getList();
                            for (T model : added)
                                if (model.getPrimaryKey() == id) {
                                    Platform.runLater(() -> comboBox.getSelectionModel().select(model));
                                    list.removeListener(this);
                                    break;
                                }
                        }
                    }
                });
        });
    }

    public static <T extends NonCompositePrimaryKeyModel> void removeItemById(ComboBox<T> comboBox, int id) {
        Platform.runLater(() -> {
            ObservableList<T> list = comboBox.getItems();
            T item = Utils.find(list, id);
            if (item != null)
                comboBox.getItems().remove(item);
            else
                list.addListener(new ListChangeListener<T>() {
                    @Override
                    public void onChanged(Change<? extends T> change) {
                        while (change.next()) {
                            List<? extends T> added = change.getList();
                            for (T model : added)
                                if (model.getPrimaryKey() == id) {
                                    comboBox.getItems().remove(model);
                                    list.removeListener(this);
                                    break;
                                }
                        }
                    }
                });
        });
    }

    private static <T extends NonCompositePrimaryKeyModel> void updateChildComboBoxItems(ComboBox<T> comboBox, ObservableList<T> items, Function<T, SimpleIntegerProperty> mapper, int id) {
        comboBox.setItems(Utils.filter(items, model -> mapper.apply(model).getValue() == id || model.getPrimaryKey() <= 0));
        comboBox.setDisable(id <= 0);
    }

    public static <T extends NonCompositePrimaryKeyModel, M extends NonCompositePrimaryKeyModel> void bindChildComboBox(ComboBox<T> parent, ComboBox<M> child, ObservableList<M> items, Function<M, SimpleIntegerProperty> mapper) {
        child.setItems(items);

        SingleSelectionModel<T> selectionModel = parent.getSelectionModel();

        T selectedItem = selectionModel.getSelectedItem();
        if (selectedItem != null) {
            int currentId = selectionModel.getSelectedItem().getPrimaryKey();
            if (currentId > 0)
                updateChildComboBoxItems(child, items, mapper, currentId);
            else
                child.setDisable(true);
        } else
            child.setDisable(true);

        selectionModel.selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null)
                updateChildComboBoxItems(child, items, mapper, newValue.getPrimaryKey());
        });
    }

    public static void disableAll(Node... nodes) {
        for (Node node : nodes)
            node.setDisable(true);
    }

    public static <M, T> void setupEditableColumn(JFXTreeTableColumn<M, T> column, Function<M, WritableValue<T>> mapper) {
        column.setOnEditCommit((TreeTableColumn.CellEditEvent<M, T> t) -> {
            M item = t.getTreeTableView().getTreeItem(t.getTreeTablePosition().getRow()).getValue();
            mapper.apply(item).setValue(t.getNewValue());
        });
    }

    public static <M, T> void setupCellValueFactory(JFXTreeTableColumn<M, T> column, Function<M, ObservableValue<T>> mapper) {
        column.setCellValueFactory((TreeTableColumn.CellDataFeatures<M, T> param) -> {
            if (column.validateValue(param)) {
                return mapper.apply(param.getValue().getValue());
            } else {
                return column.getComputedValue(param);
            }
        });
    }

    public static void setupRequiredTextFields(JFXTextField... textFields) {
        for (JFXTextField textField : textFields)
            setupValidatedTextField(textField, "Esse campo é obrigatório.");
    }

    public static void setupValidatedTextField(JFXTextField textField, String errorMessage) {
        RequiredFieldValidator validator = new RequiredFieldValidator();
        validator.setMessage(errorMessage);
        textField.getValidators().add(validator);

        textField.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                textField.validate();
            }
        });
    }

    public static void showTextFieldValidationError(JFXTextField textField) {
        if (textField.getActiveValidator() == null)
            textField.validate(); //exibe a mensagem de erro se o campo não tinha recebido foco anteriormente
    }

    public static boolean validateRequiredTextFields(JFXTextField... textFields) {
        boolean flag = true;
        for (JFXTextField textField : textFields)
            if (textField.getText().isEmpty()) {
                if (flag)
                    textField.requestFocus();
                showTextFieldValidationError(textField);
                flag = false;
            }
        return flag;
    }

    public static boolean validateRequired(ComboBoxBase... nodes) {
        for (ComboBoxBase node : nodes) {
            Object value = node.getValue();
            if (value == null || (value instanceof NonCompositePrimaryKeyModel && ((NonCompositePrimaryKeyModel) value).getPrimaryKey() <= 0)) {
                node.requestFocus();
                new AlertDialog(MainController.getRoot(), "Aviso", "Há campos obrigatórios não preenchidos.").show();
                return false;
            }
        }
        return true;
    }

    public static void tintDark(ImageView imageView) {
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-1);
        imageView.setEffect(colorAdjust);
    }
}
