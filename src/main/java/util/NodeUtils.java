package util;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.TreeTableColumn;

import java.util.function.Function;

public class NodeUtils {

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
            if (comboBox.getSelectionModel().isEmpty()) {
                if (flag)
                    comboBox.requestFocus();
                flag = false;
            }
        return flag;
    }
}
