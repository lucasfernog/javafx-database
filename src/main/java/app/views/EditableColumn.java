package app.views;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.cells.editors.DoubleTextFieldEditorBuilder;
import com.jfoenix.controls.cells.editors.IntegerTextFieldEditorBuilder;
import com.jfoenix.controls.cells.editors.TextFieldEditorBuilder;
import com.jfoenix.controls.cells.editors.base.EditorNodeBuilder;
import com.jfoenix.controls.cells.editors.base.GenericEditableTreeTableCell;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TreeTableColumn;

public class EditableColumn<S, T> extends JFXTreeTableColumn<S, T> {

    private SimpleStringProperty type = new SimpleStringProperty("string");

    public EditableColumn() {
        setCellFactory((TreeTableColumn<S, T> param) -> new GenericEditableTreeTableCell<>(getEditorNodeBuilder()));
    }

    private EditorNodeBuilder getEditorNodeBuilder() {
        switch (getType()) {
            case "integer":
            case "int":
                return new IntegerTextFieldEditorBuilder();
            case "double":
            case "float":
                return new DoubleTextFieldEditorBuilder();
            default:
                return new TextFieldEditorBuilder();
        }
    }

    public String getType() {
        return typeProperty().get();
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        typeProperty().set(type);
    }
}
