package util;

import com.jfoenix.controls.JFXTreeTableColumn;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.TreeTableColumn;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Utils {
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

    public static String join(CharSequence delimiter, Object[] tokens) {
        StringBuilder sb = new StringBuilder();
        boolean firstTime = true;
        for (Object token : tokens) {
            if (firstTime) {
                firstTime = false;
            } else {
                sb.append(delimiter);
            }
            sb.append(token);
        }
        return sb.toString();
    }

    public static String join(CharSequence delimiter, Iterable tokens) {
        StringBuilder sb = new StringBuilder();
        boolean firstTime = true;
        for (Object token : tokens) {
            if (firstTime) {
                firstTime = false;
            } else {
                sb.append(delimiter);
            }
            sb.append(token);
        }
        return sb.toString();
    }

    public static <T> String joinToSql(CharSequence delimiter, List<T> tokens) {
        if (tokens.get(0) instanceof String) {
            List<String> strTokens = new ArrayList<>();
            for (Object token : tokens) {
                strTokens.add("'" + token + "'");
            }
            return join(delimiter, strTokens);
        } else
            return join(delimiter, tokens);
    }
}
