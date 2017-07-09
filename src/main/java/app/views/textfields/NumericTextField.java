package app.views.textfields;

import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ObservableValue;
import util.Utils;

public class NumericTextField extends JFXTextField {
    private Character separator = null;

    public NumericTextField() {
        super();
        initialize();
    }

    public NumericTextField(String text) {
        super(text);
        initialize();
    }

    public void setSeparator(char separator) {
        this.separator = separator;
    }

    public String applyPattern() {
        return getText().replaceAll("[^\\d" + (separator == null ? "]" : ("/" + separator + "]")), "");
    }

    void initialize() {
        lengthProperty().addListener((ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) -> {
            String newText = applyPattern();
            if (separator != null) {
                int countSeparator = Utils.countOccurrences(newText, separator);
                if (countSeparator > 1)
                    newText = Utils.replaceLast(newText, String.valueOf(separator), "");
                else if (countSeparator == 1 && newValue.intValue() == 1)
                    newText = "";
            }

            setText(newText);
        });

        focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (separator != null && !newValue) {
                String text = applyPattern();
                int length = text.length();
                if (length > 0) {
                    if (text.charAt(length - 1) == separator)
                        setText(text + "00");
                    else if (length > 1 && text.charAt(length - 2) == separator)
                        setText(text + "0");
                }
            }
        });
    }
}
