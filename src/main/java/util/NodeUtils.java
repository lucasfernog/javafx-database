package util;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.validation.RequiredFieldValidator;
import database.model.Model;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;

import java.util.List;
import java.util.function.Function;

public class NodeUtils {

    public static <T extends Model> void selectItemById(ComboBox<T> comboBox, int id) {
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

    private static <T extends Model> void updateChildComboBoxItems(ComboBox<T> comboBox, ObservableList<T> items, Function<T, SimpleIntegerProperty> mapper, int id) {
        comboBox.setItems(Utils.filter(items, model -> mapper.apply(model).getValue() == id || model.getPrimaryKey() <= 0));
        comboBox.setDisable(id <= 0);
    }

    public static <T extends Model, M extends Model> void bindChildComboBox(ComboBox<T> parent, ComboBox<M> child, ObservableList<M> items, Function<M, SimpleIntegerProperty> mapper) {
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

        selectionModel.selectedItemProperty().addListener((obs, oldValue, newValue) ->
                updateChildComboBoxItems(child, items, mapper, newValue.getPrimaryKey()));
    }

    public static void disableAll(Node... nodes) {
        for (Node node : nodes)
            node.setDisable(true);
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

    public static void numericTextInputControl(TextInputControl textInputControl) {
        textInputControl.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textInputControl.setText(newValue.replaceAll("[^\\d]", ""));
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

    public static boolean validateRequiredComboBoxes(JFXComboBox... comboBoxes) {
        boolean flag = true;
        for (JFXComboBox comboBox : comboBoxes)
            if (comboBox.getSelectionModel().getSelectedIndex() <= 0) {
                if (flag)
                    comboBox.requestFocus();
                flag = false;
            }
        return flag;
    }

    public static void tintDark(ImageView imageView) {
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-1);
        imageView.setEffect(colorAdjust);
    }
}
