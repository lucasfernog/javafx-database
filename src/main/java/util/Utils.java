package util;

import com.jfoenix.controls.JFXTreeTableColumn;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeTableColumn;

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
}
